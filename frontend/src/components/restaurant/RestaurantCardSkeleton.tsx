import React from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";

export const RestaurantCardSkeleton: React.FC = () => {
  return (
    <Card className="p-0 animate-pulse flex flex-col overflow-hidden">
      <Skeleton className="w-full h-48 bg-gray-400 rounded-b-none" />

      <CardContent className="flex flex-col p-4 gap-2">
        <Skeleton className="bg-gray-400 w-24 h-5 rounded-full mb-1" />

        <Skeleton className="bg-gray-400 w-3/4 h-6 rounded mb-2" />

        <Skeleton className="bg-gray-400 w-1/2 h-4 rounded mb-1" />
        <Skeleton className="bg-gray-400 w-5/6 h-4 rounded mb-1" />

        <div className="grid grid-cols-2 gap-2 mt-2">
          <Skeleton className="bg-gray-400 w-24 h-4 rounded" />
          <Skeleton className="bg-gray-400 w-20 h-4 rounded" />
          <Skeleton className="bg-gray-400 w-28 h-4 rounded" />
          <Skeleton className="bg-gray-400 w-24 h-4 rounded" />
          <Skeleton className="bg-gray-400 w-20 h-4 rounded" />
        </div>

        <Skeleton className="bg-gray-400 w-full h-9 rounded-md mt-4" />
      </CardContent>
    </Card>
  );
};
