import React from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { InfoIcon } from "lucide-react";

export const OrderCardSkeleton: React.FC = () => {
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
          <Skeleton className="w-40 h-4" />
          <Badge className="px-3 py-1 rounded-full bg-gray-200 text-gray-400">
            <Skeleton className="w-20 h-4" />
          </Badge>
        </div>

        <div className="flex flex-col gap-2">
          <Skeleton className="w-32 h-4" />
          <Skeleton className="w-3/4 h-4" />
        </div>
        <div className="flex flex-col gap-2">
          <Skeleton className="w-24 h-4" />
          <Skeleton className="w-2/3 h-4" />
        </div>

        <div className="flex flex-col gap-2">
          <Skeleton className="w-36 h-4" />
          <ul className="divide-y divide-gray-200">
            {Array.from({ length: 3 }).map((_, idx) => (
              <li key={idx} className="py-2 flex justify-between items-center">
                <div className="flex items-center gap-3">
                  <Skeleton className="w-12 h-12 rounded" />
                  <Skeleton className="w-24 h-4" />
                </div>
                <div className="flex gap-4">
                  <Skeleton className="w-12 h-4" />
                  <Skeleton className="w-16 h-4" />
                </div>
              </li>
            ))}
          </ul>
        </div>

        <div className="flex justify-between font-semibold text-lg">
          <Skeleton className="w-16 h-5" />
          <Skeleton className="w-20 h-5" />
        </div>

        <div className="flex justify-end">
          <Button variant="outline" className="flex items-center gap-2 cursor-not-allowed">
            <InfoIcon className="w-4 h-4" />
            <Skeleton className="w-20 h-4" />
          </Button>
        </div>
      </CardContent>
    </Card>
  );
};
