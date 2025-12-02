import React from "react";
import {
  Pagination as UIPagination,
  PaginationContent,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination";

interface PaginationProps {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
  isFetching?: boolean;
}

export const Pagination: React.FC<PaginationProps> = ({
  currentPage,
  totalPages,
  onPageChange,
  isFetching = false,
}) => {
  const pages = Array.from({ length: totalPages }, (_, i) => i);

  return (
    <UIPagination>
      <PaginationContent className="justify-center mt-6 gap-2">
        <PaginationPrevious
          onClick={() => onPageChange(Math.max(0, currentPage - 1))}
          className={
            currentPage === 0
              ? "opacity-50 pointer-events-none cursor-pointer select-none"
              : ""
          }
        />

        {pages.map((p) => (
          <PaginationItem className="cursor-pointer select-none" key={p}>
            <PaginationLink
              onClick={() => {
                if (!isFetching) onPageChange(p);
              }}
              isActive={p === currentPage}
            >
              {p + 1}
            </PaginationLink>
          </PaginationItem>
        ))}

        <PaginationNext
          onClick={() => onPageChange(Math.min(totalPages - 1, currentPage + 1))}
          className={
            currentPage === totalPages - 1
              ? "opacity-50 pointer-events-none cursor-pointer select-none"
              : ""
          }
        />
      </PaginationContent>
    </UIPagination>
  );
};
