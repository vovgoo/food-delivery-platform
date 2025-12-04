import React from 'react';
import { toast } from 'sonner';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation, useQueryClient } from '@tanstack/react-query';

import { Form, FormField, FormItem, FormControl } from '@/components/ui/form';
import { FileInput } from '@/components/input/FileInput';
import { SpinnerButton } from '@/components/button/SpinnerButton';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/card';
import { Skeleton } from '@/components/ui/skeleton';
import { Button } from '@/components/ui/button';
import { Trash2 } from 'lucide-react';

import type { RestaurantResponse, DishResponse, ImageResponse } from '@/api';
import { uploadImageSchema, type UploadImageFormData } from '@/schemas/common/upload-image.schema';
import { dishService } from '@/api/services/dish';

interface UpdateDishImagesFormProps {
  dish?: DishResponse;
  restaurant?: RestaurantResponse;
  isPending: boolean;
}

export const UpdateDishImagesForm: React.FC<UpdateDishImagesFormProps> = ({
  dish,
  restaurant,
  isPending,
}) => {
  const queryClient = useQueryClient();

  const form = useForm<UploadImageFormData>({
    resolver: zodResolver(uploadImageSchema),
  });

  const addMutation = useMutation({
    mutationFn: (file: File) => dishService.addImage(restaurant!.id, dish!.id, file),
    onSuccess: () => {
      toast.success('Фото добавлено');
      queryClient.invalidateQueries({ queryKey: ['admin-dish'] });
      form.reset();
    },
    onError: () => toast.error('Не удалось добавить фото'),
  });

  const removeMutation = useMutation({
    mutationFn: ({ imageId }: { imageId: string }) =>
      dishService.removeImage(restaurant!.id, dish!.id, imageId),
    onSuccess: () => {
      toast.success('Фото удалено');
      queryClient.invalidateQueries({ queryKey: ['admin-dish'] });
    },
    onError: () => toast.error('Не удалось удалить фото'),
  });

  const onSubmit = (data: UploadImageFormData) => {
    addMutation.mutate(data.profileImage);
  };

  return (
    <Card className="w-full">
      <CardHeader>
        <CardTitle>Галерея блюда</CardTitle>
        <CardDescription>Добавьте новые фотографии или удалите существующие</CardDescription>
      </CardHeader>

      <CardContent className="flex flex-col">
        <Form {...form}>
          <FormField
            control={form.control}
            name="profileImage"
            render={({ field, fieldState }) => (
              <FormItem>
                <FormControl>
                  {isPending ? (
                    <Skeleton className="bg-gray-400 h-10 w-full rounded" />
                  ) : (
                    <FileInput
                      name={field.name}
                      control={form.control}
                      accept="image/*"
                      multiple={false}
                      error={fieldState.error?.message}
                    />
                  )}
                </FormControl>
              </FormItem>
            )}
          />
          <div className="flex gap-4 mt-4 justify-end">
            {isPending ? (
              <Skeleton className="bg-gray-400 h-10 w-40 rounded" />
            ) : (
              <SpinnerButton
                text="Добавить фото"
                loadingText="Загружаем..."
                isLoading={addMutation.isPending}
                onClick={form.handleSubmit(onSubmit)}
                disabled={!form.watch('profileImage')}
              />
            )}
          </div>
        </Form>

        {isPending ? (
          <div className="grid grid-cols-3 gap-4 mt-4">
            {[...Array(6)].map((_, idx) => (
              <Skeleton key={idx} className="bg-gray-400 h-32 w-full rounded" />
            ))}
          </div>
        ) : (
          <div className="grid grid-cols-3 gap-4 mt-4">
            {dish?.images?.map((image: ImageResponse) => (
              <div key={image.id} className="relative group">
                <img src={image.url} alt="Dish" className="h-64 w-full object-cover rounded" />
                <Button
                  variant="destructive"
                  size="icon"
                  className="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition"
                  onClick={() => removeMutation.mutate({ imageId: image.id })}
                >
                  <Trash2 className="h-4 w-4" />
                </Button>
              </div>
            ))}
          </div>
        )}
      </CardContent>
    </Card>
  );
};
