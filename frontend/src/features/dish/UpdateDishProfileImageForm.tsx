import React from 'react';
import { toast } from 'sonner';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation, useQueryClient } from '@tanstack/react-query';

import { Form, FormField, FormItem, FormControl } from '@/components/ui/form';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/card';
import { Skeleton } from '@/components/ui/skeleton';

import { dishService, type DishResponse, type RestaurantResponse } from '@/api';

import { ImageIcon } from 'lucide-react';
import { uploadImageSchema, type UploadImageFormData } from '@/schemas';
import { FileInput, SpinnerButton } from '@/components';

interface UpdateDishProfileImageFormProps {
  dish?: DishResponse;
  restaurant?: RestaurantResponse;
  isPending: boolean;
}

export const UpdateDishProfileImageForm: React.FC<UpdateDishProfileImageFormProps> = ({
  dish,
  restaurant,
  isPending,
}) => {
  const queryClient = useQueryClient();

  const form = useForm<UploadImageFormData>({
    resolver: zodResolver(uploadImageSchema),
  });

  const uploadMutation = useMutation({
    mutationFn: (file: File) => dishService.setProfileImage(restaurant!.id, dish!.id, file),
    onSuccess: () => {
      toast.success('Фото блюда обновлено');
      queryClient.invalidateQueries({ queryKey: ['admin-dish'] });
      form.reset();
    },
    onError: () => {
      toast.error('Не удалось обновить фото блюда');
    },
  });

  const removeMutation = useMutation({
    mutationFn: () => dishService.removeProfileImage(restaurant!.id, dish!.id),
    onSuccess: () => {
      toast.success('Фото блюда удалено');
      queryClient.invalidateQueries({ queryKey: ['admin-dish'] });
    },
    onError: () => {
      toast.error('Не удалось удалить фото блюда');
    },
  });

  const onSubmit = (data: UploadImageFormData) => {
    uploadMutation.mutate(data.profileImage);
  };

  return (
    <Card className="w-full">
      <CardHeader>
        <CardTitle>Фото блюда</CardTitle>
        <CardDescription>
          Добавьте или измените фото блюда <strong>{dish?.name ?? '...'}</strong>
        </CardDescription>
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
              {dish?.profileImage?.url ? (
                <img
                  src={dish.profileImage.url}
                  alt="Dish"
                  className="w-full h-64 object-cover rounded"
                />
              ) : (
                <div className="w-full h-64 bg-gray-300 flex items-center justify-center rounded">
                  <ImageIcon className="w-12 h-12 text-white" />
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
                  {dish?.profileImage?.url && (
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
