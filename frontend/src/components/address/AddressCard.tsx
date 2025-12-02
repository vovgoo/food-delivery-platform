import React from "react";
import { Card, CardContent } from "@/components/ui/card";
import { MapPinIcon, MoreHorizontalIcon, StarIcon, Trash2Icon } from "lucide-react";
import { toast } from "sonner";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { addressService, type AddressResponse } from "@/api";
import {
  DropdownMenu,
  DropdownMenuTrigger,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
} from "@/components/ui/dropdown-menu";

interface AddressCardProps {
  address: AddressResponse;
}

export const AddressCard: React.FC<AddressCardProps> = ({ address }) => {
  const queryClient = useQueryClient();

  const setDefaultMutation = useMutation({
    mutationFn: () => addressService.setDefault(address.id),
    onSuccess: () => {
      toast.success("Адрес установлен как основной");
      queryClient.invalidateQueries({ queryKey: ["me"] });
      queryClient.invalidateQueries({ queryKey: ["addresses"] });
    },
    onError: () => {
      toast.error("Не удалось установить адрес по умолчанию");
    },
  });

  const deleteMutation = useMutation({
    mutationFn: () => addressService.remove(address.id),
    onSuccess: () => {
      toast.success("Адрес удален");
      queryClient.invalidateQueries({ queryKey: ["me"] });
      queryClient.invalidateQueries({ queryKey: ["addresses"] });
    },
    onError: () => {
      toast.error("Не удалось удалить адрес");
    },
  });

  return (
    <Card
        className={`min-h-36 flex gap-4 p-4 border rounded-lg items-start text-sm ${
          address.isDefault
            ? "bg-black text-white border-black"
            : "bg-white"
        }`}
      >

      <CardContent className="p-0 flex-1 flex flex-col">
        <div className="flex items-center gap-2">
          <MapPinIcon
            className={`w-6 h-6 ${
              address.isDefault ? "text-orange-500" : "text-gray-400"
            }`}
          />
          <div className="font-medium text-sm">
            {address.country}, {address.zip}, {address.state}, {address.city}, {address.street}, {address.house}
            {address.building && `, ${address.building}`}
            {address.apartment && `, ${address.apartment}`} 
          </div>
        </div>
        {address.deliveryInstructions && (
          <div className="mt-5 text-sm">
            {address.deliveryInstructions}
          </div>
        )}
      </CardContent>

      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <button className="ml-auto p-1 cursor-pointer">
            <MoreHorizontalIcon className="w-4 h-4" />
          </button>
        </DropdownMenuTrigger>

        <DropdownMenuContent align="end">
          {!address.isDefault && (
            <>
              <DropdownMenuItem
                className="flex items-center gap-2 cursor-pointer"
                onClick={() => setDefaultMutation.mutate()}
              >
                <StarIcon className="w-4 h-4 mr-2" />
                Сделать основным
              </DropdownMenuItem>

              <DropdownMenuSeparator />
            </>
          )}
          <DropdownMenuItem
            className="flex items-center gap-2 cursor-pointer"
            onClick={() => deleteMutation.mutate()}
          >
            <Trash2Icon className="w-4 h-4 mr-2" />
            Удалить
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
    </Card>
  );
};
