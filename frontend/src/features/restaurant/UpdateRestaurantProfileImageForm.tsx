import React from 'react';
import { toast } from 'sonner';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation, useQueryClient } from '@tanstack/react-query';

import { Form, FormField, FormItem, FormControl } from '@/components/ui/form';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/card';
import { Skeleton } from '@/components/ui/skeleton';
import { restaurantService, type RestaurantResponse } from '@/api';
import { Building2 } from 'lucide-react';
import { uploadImageSchema, type UploadImageFormData } from '@/schemas';
import { FileInput, SpinnerButton } from '@/components';

interface UpdateRestaurantProfileImageFormProps {
  restaurant?: RestaurantResponse;
  isPending: boolean;
}

export const UpdateRestaurantProfileImageForm: React.FC<UpdateRestaurantProfileImageFormProps> = ({
  restaurant,
  isPending,
}) => {
  const queryClient = useQueryClient();

  const form = useForm<UploadImageFormData>({
    resolver: zodResolver(uploadImageSchema),
  });

  const uploadMutation = useMutation({
    mutationFn: (file: File) => restaurantService.setProfileImage(restaurant!.id, file),
    onSuccess: () => {
      toast.success('Фото профиля обновлено');
      queryClient.invalidateQueries({ queryKey: ['admin-restaurant'] });
      form.reset();
    },
    onError: () => {
      toast.error('Не удалось обновить фото профиля');
    },
  });

  const removeMutation = useMutation({
    mutationFn: () => restaurantService.removeProfileImage(restaurant!.id),
    onSuccess: () => {
      toast.success('Фото профиля удалено');
      queryClient.invalidateQueries({ queryKey: ['admin-restaurant'] });
    },
    onError: () => {
      toast.error('Не удалось удалить фото профиля');
    },
  });

  const onSubmit = (data: UploadImageFormData) => {
    uploadMutation.mutate(data.profileImage);
  };

  return (
    <Card className="w-full">
      <CardHeader>
        <CardTitle>Фото профиля ресторана</CardTitle>
        <CardDescription>Добавьте или измените фото профиля ресторана</CardDescription>
      </CardHeader>

      <CardContent>
        {isPending ? (
          <div className="flex gap-4">
            <Skeleton className="bg-gray-400 h-64 w-1/2 rounded" />
            <div className="flex-1 flex flex-col justify-between gap-4">
              <Skeleton className="bg-gray-400 h-10 w-full rounded" />
              <div className="flex gap-4">
                <Skeleton className="bg-gray-400 h-10 flex-1 rounded" />
                <Skeleton className="bg-gray-400 h-10 flex-1 rounded" />
              </div>
            </div>
          </div>
        ) : (
          <div className="flex gap-4">
            <div className="w-1/2">
              {restaurant?.profileImage?.url ? (
                <img
                  src={restaurant.profileImage.url}
                  alt="Profile"
                  className="w-full h-64 object-cover rounded"
                />
              ) : (
                <div className="w-full h-64 bg-gray-300 flex items-center justify-center rounded">
                  <Building2 className="w-12 h-12 text-white" />
                </div>
              )}
            </div>

            <div className="flex-1 flex flex-col justify-between gap-4">
              <Form {...form}>
                <FormField
                  control={form.control}
                  name="profileImage"
                  render={({ field, fieldState }) => (
                    <FormItem>
                      <FormControl>
                        <FileInput
                          name={field.name}
                          control={form.control}
                          accept="image/*"
                          multiple={false}
                          error={fieldState.error?.message}
                        />
                      </FormControl>
                    </FormItem>
                  )}
                />

                <div className="flex gap-4 mt-4">
                  {restaurant?.profileImage?.url && (
                    <SpinnerButton
                      text="Удалить"
                      loadingText="Удаляем..."
                      isLoading={removeMutation.isPending}
                      onClick={() => removeMutation.mutate()}
                    />
                  )}
                  <SpinnerButton
                    text="Загрузить"
                    loadingText="Загружаем..."
                    isLoading={uploadMutation.isPending}
                    onClick={form.handleSubmit(onSubmit)}
                    disabled={!form.watch('profileImage')}
                  />
                </div>
              </Form>
            </div>
          </div>
        )}
      </CardContent>
    </Card>
  );
};
