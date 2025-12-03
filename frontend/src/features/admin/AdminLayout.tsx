import React, { useEffect } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import { AppRoutes } from "@/routes";
import { useQuery } from "@tanstack/react-query";
import { userService, type UserResponse } from "@/api";
import { AdminSidebar } from "./AdminSidebar";
import { Spinner } from "@/components/ui/spinner";

export const AdminLayout: React.FC = () => {
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("accessToken");
        if (!token) navigate(AppRoutes.MAIN);
    }, [navigate]);

    const { data: user, isPending } = useQuery<UserResponse>({
        queryKey: ["me"],
        queryFn: () => userService.me()
    });

    useEffect(() => {
        const token = localStorage.getItem("accessToken");
        if (!token) {
        navigate(AppRoutes.MAIN);
        return;
        }

        if (!isPending && user) {
        const isAdmin = user.roles?.some(role => role.name === "ADMIN");
        if (!isAdmin) navigate(AppRoutes.MAIN);
        }
    }, [navigate, user, isPending]);

     if (isPending) return <div className="w-full min-h-screen flex items-center justify-center"><Spinner/></div>;

  return (
    <div className="flex min-h-screen my-10 gap-x-10">
        <AdminSidebar />
        <main className="flex flex-col flex-1 gap-6">
            <Outlet />
        </main>
    </div>
  );
};
