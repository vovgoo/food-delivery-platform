import React from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Skeleton } from "@/components/ui/skeleton";

export const OrderCardAdminSkeleton: React.FC = () => {
  return (
    <Card className="w-full mb-6 animate-pulse">
      <CardHeader className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-2">
        <CardTitle className="text-lg font-semibold">
          <Skeleton className="w-40 h-6" />
        </CardTitle>
        <Badge className="px-3 py-1 rounded-full bg-gray-200 text-gray-400">
          <Skeleton className="w-20 h-4" />
        </Badge>
      </CardHeader>

      <CardContent className="flex flex-col gap-4">
        <div className="flex justify-between items-center">
          <Skeleton className="w-48 h-4" />
          <Badge className="px-3 py-1 rounded-full bg-gray-200 text-gray-400">
            <Skeleton className="w-20 h-4" />
          </Badge>
        </div>

        <div className="mt-2">
          <Skeleton className="w-40 h-10 rounded" />
        </div>
      </CardContent>
    </Card>
  );
};
