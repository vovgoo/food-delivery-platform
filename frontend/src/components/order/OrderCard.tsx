import React from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import type { PaymentStatus, OrderStatus, PaymentMethod, OrderShortResponse } from '@/api';
import { Button } from '../ui/button';
import { useNavigate } from 'react-router-dom';
import { InfoIcon } from 'lucide-react';
import { AppRoutes } from '@/routes';

const ORDER_STATUS_RU: Record<OrderStatus, string> = {
  CREATED: 'Создан',
  CONFIRMED: 'Подтверждён',
  PREPARING: 'Готовится',
  READY: 'Готов',
  DELIVERING: 'Доставляется',
  COMPLETED: 'Завершён',
  CANCELLED: 'Отменён',
};

const PAYMENT_STATUS_RU: Record<PaymentStatus, string> = {
  PENDING: 'Ожидается',
  PROCESSING: 'В обработке',
  COMPLETED: 'Оплачено',
  FAILED: 'Не удалось',
  CANCELLED: 'Отменено',
  REFUNDED: 'Возвращено',
};

const PAYMENT_METHOD_RU: Record<PaymentMethod, string> = {
  CREDIT_CARD: 'Кредитная карта',
  DEBIT_CARD: 'Дебетовая карта',
  PAYPAL: 'PayPal',
  APPLE_PAY: 'Apple Pay',
  GOOGLE_PAY: 'Google Pay',
  BANK_TRANSFER: 'Банковский перевод',
  CASH_ON_DELIVERY: 'Наличные при доставке',
};

interface OrderCardProps {
  order: OrderShortResponse;
}

const getStatusBadgeClass = (status: OrderStatus | PaymentStatus) => {
  switch (status) {
    case 'COMPLETED':
      return 'bg-green-100 text-green-700';
    case 'CANCELLED':
    case 'FAILED':
      return 'bg-red-100 text-red-700';
    default:
      return 'bg-yellow-100 text-yellow-700';
  }
};

const formatOrderDate = (dateString: string) => {
  const date = new Date(dateString);
  return new Intl.DateTimeFormat('ru-RU', {
    day: '2-digit',
    month: 'long',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    hour12: false,
  }).format(date);
};

export const OrderCard: React.FC<OrderCardProps> = ({ order }) => {
  const navigate = useNavigate();

  return (
    <Card className="w-full mb-6">
      <CardHeader className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-2">
        <CardTitle className="text-lg font-semibold">
          Заказ #{order.id} —{' '}
          <span className="text-gray-600">{formatOrderDate(order.orderDate)}</span>
        </CardTitle>
        <Badge className={`px-3 py-1 rounded-full ${getStatusBadgeClass(order.status)}`}>
          {ORDER_STATUS_RU[order.status]}
        </Badge>
      </CardHeader>

      <CardContent className="flex flex-col gap-4">
        <div className="flex justify-between items-center">
          <div>
            <span className="font-semibold">Метод оплаты: </span>
            <span>{PAYMENT_METHOD_RU[order.payment.method]}</span>
          </div>
          <Badge className={`px-3 py-1 rounded-full ${getStatusBadgeClass(order.payment.status)}`}>
            {PAYMENT_STATUS_RU[order.payment.status]}
          </Badge>
        </div>
        <div className="mt-4 flex justify-end">
          <Button
            variant="outline"
            className="flex items-center gap-2"
            onClick={() => navigate(AppRoutes.PROFILE_ORDER_DETAILS.replace(':orderId', order.id))}
          >
            <InfoIcon className="w-4 h-4" />
            Подробнее
          </Button>
        </div>
      </CardContent>
    </Card>
  );
};
