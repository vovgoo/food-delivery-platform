import React, { useEffect } from "react";
import { useQuery } from "@tanstack/react-query";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "sonner";
import { orderService } from "@/api/services/order/order.service";
import type {
  OrderResponse,
  OrderItemResponse,
  PaymentStatus,
  OrderStatus,
  PaymentMethod,
} from "@/api";
import { AppRoutes } from "@/routes";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Skeleton } from "@/components/ui/skeleton";
import { ImageIcon } from "lucide-react";

const ORDER_STATUS_RU: Record<OrderStatus, string> = {
  CREATED: "Создан",
  CONFIRMED: "Подтверждён",
  PREPARING: "Готовится",
  READY: "Готов",
  DELIVERING: "Доставляется",
  COMPLETED: "Завершён",
  CANCELLED: "Отменён",
};

const PAYMENT_STATUS_RU: Record<PaymentStatus, string> = {
  PENDING: "Ожидается",
  PROCESSING: "В обработке",
  COMPLETED: "Оплачено",
  FAILED: "Не удалось",
  CANCELLED: "Отменено",
  REFUNDED: "Возвращено",
};

const PAYMENT_METHOD_RU: Record<PaymentMethod, string> = {
  CREDIT_CARD: "Кредитная карта",
  DEBIT_CARD: "Дебетовая карта",
  PAYPAL: "PayPal",
  APPLE_PAY: "Apple Pay",
  GOOGLE_PAY: "Google Pay",
  BANK_TRANSFER: "Банковский перевод",
  CASH_ON_DELIVERY: "Наличные при доставке",
};

const getStatusBadgeClass = (status: OrderStatus | PaymentStatus) => {
  switch (status) {
    case "COMPLETED":
      return "bg-green-100 text-green-700";
    case "CANCELLED":
    case "FAILED":
      return "bg-red-100 text-red-700";
    default:
      return "bg-yellow-100 text-yellow-700";
  }
};

const formatOrderDate = (dateString: string) => {
  const date = new Date(dateString);
  return new Intl.DateTimeFormat("ru-RU", {
    day: "2-digit",
    month: "long",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
  }).format(date);
};

const ImageSkeleton: React.FC<{ src?: string; alt?: string }> = ({ src, alt }) => {
  if (src)
    return <img src={src} alt={alt} className="w-16 h-16 object-cover rounded" />;
  return (
    <div className="w-16 h-16 bg-gray-300 flex items-center justify-center rounded">
      <ImageIcon className="w-6 h-6 text-white" />
    </div>
  );
};

const OrderDetailsSkeleton: React.FC = () => (
  <Card className="w-full mb-6 animate-pulse">
    <CardHeader className="flex justify-between items-center mb-4">
      <CardTitle className="text-lg font-semibold">
        <Skeleton className="w-60 h-6" />
      </CardTitle>
      <Badge className="px-3 py-1 rounded-full bg-gray-200 text-gray-400">
        <Skeleton className="w-24 h-4" />
      </Badge>
    </CardHeader>
    <CardContent className="flex flex-col gap-4">
      <Skeleton className="w-40 h-4" />
      <Skeleton className="w-32 h-4" />
      {Array.from({ length: 3 }).map((_, idx) => (
        <div key={idx} className="flex justify-between items-center">
          <Skeleton className="w-24 h-4" />
          <Skeleton className="w-16 h-4" />
        </div>
      ))}
    </CardContent>
  </Card>
);

const OrderDetailsCard: React.FC<{ order: OrderResponse }> = ({ order }) => (
  <Card className="w-full mb-6">
    <CardHeader className="flex justify-between items-center mb-4">
      <CardTitle className="text-lg font-semibold">
        Заказ #{order.id} —{" "}
        <span className="text-gray-600">{formatOrderDate(order.orderDate)}</span>
      </CardTitle>
      <Badge className={`px-3 py-1 rounded-full ${getStatusBadgeClass(order.status)}`}>
        {ORDER_STATUS_RU[order.status]}
      </Badge>
    </CardHeader>
    <CardContent className="flex flex-col gap-4">
      <div>
        <h3 className="font-semibold mb-1">Адрес доставки</h3>
        <div className="text-gray-700">
          {order.address.city}, {order.address.street} {order.address.house}
          {order.address.apartment ? `, кв. ${order.address.apartment}` : ""}
        </div>
      </div>

      <div>
        <h3 className="font-semibold mb-1">Ресторан</h3>
        <div className="flex items-center gap-3">
          <ImageSkeleton src={order.restaurant.profileImageUrl} alt={order.restaurant.name} />
          <div>
            <div>{order.restaurant.name}</div>
            <div className="text-gray-600 text-sm">
              {order.restaurant.cuisine} — {order.restaurant.address} — {order.restaurant.phone}
            </div>
          </div>
        </div>
      </div>

      <div>
        <h3 className="font-semibold mb-2">Состав заказа</h3>
        <ul className="divide-y divide-gray-200">
          {order.items.map((item: OrderItemResponse) => (
            <li key={item.id} className="py-2 flex justify-between items-center">
              <div className="flex items-center gap-3">
                <ImageSkeleton src={item.dish.profileImageUrl} alt={item.dish.name} />
                <span>{item.dish.name}</span>
              </div>
              <div className="flex gap-4">
                <span>{item.quantity} шт.</span>
               <span>{(item.price * item.quantity).toFixed(2)} BYN</span>
              </div>
            </li>
          ))}
        </ul>
      </div>

      <div className="flex justify-between font-semibold text-lg">
        <span>Итого:</span>
        <span>{order.totalPrice.toFixed(2)} BYN</span>
      </div>

      <div className="flex justify-between items-center">
        <div>
          <span className="font-semibold">Метод оплаты: </span>
          <span>{PAYMENT_METHOD_RU[order.payment.method]}</span>
        </div>
        <Badge className={`px-3 py-1 rounded-full ${getStatusBadgeClass(order.payment.status)}`}>
          {PAYMENT_STATUS_RU[order.payment.status]}
        </Badge>
      </div>
    </CardContent>
  </Card>
);

const ProfileOrderDetailsPage: React.FC = () => {
  const { orderId } = useParams<{ orderId: string }>();
  const navigate = useNavigate();

  React.useEffect(() => {
    const token = localStorage.getItem("accessToken");
    if (!token) navigate(AppRoutes.MAIN);
  }, [navigate]);

  const { data: data, isPending, isError, error } = useQuery<OrderResponse>({
    queryKey: ["order"],
    queryFn: () => orderService.get(orderId!),
    retry: (failureCount, err: any) => {
      if (err?.response?.status === 404) return false;
      return failureCount < 1;
    },
  });

  useEffect(() => {
    if (isError) {
      if ((error as any)?.response?.status === 404 ) {
        toast.error("Заказ не найден");
        navigate(AppRoutes.PROFILE_ORDERS);
      } else {
        toast.error("Произошла ошибка при загрузке ресторана");
      }
    }
  }, [isError]);

  return (
    <div className="w-full flex flex-col gap-y-6 h-full">
      <h1 className="text-3xl font-bold">Информация о заказе</h1>
      {isPending && <OrderDetailsSkeleton />}
      {data && <OrderDetailsCard order={data} />}
    </div>
  );
};

export default ProfileOrderDetailsPage;
