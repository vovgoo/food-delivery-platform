import React from "react";
import { Card, CardContent } from "@/components/ui/card";

export const AddressCardSkeleton: React.FC = () => {
  return (
    <Card className="flex min-h-36 gap-4 p-4 border rounded-lg items-start text-sm bg-gray-100 animate-pulse">
      <CardContent className="p-0 flex-1 flex flex-col">
        <div className="flex items-center gap-2">
          <div className="w-8 h-6 bg-gray-400 rounded"></div>
          <div className="flex flex-col gap-1 w-full">
            <div className="w-52 h-4 bg-gray-400 rounded"></div>
          </div>
        </div>
        <div className="mt-5 h-4 bg-gray-400 rounded w-32"></div>
      </CardContent>

      <div className="ml-auto flex flex-col gap-2">
        <div className="w-4 h-4 bg-gray-400 rounded"></div>
      </div>
    </Card>
  );
};
