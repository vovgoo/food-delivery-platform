import { AdminOrderList } from "@/features/order/AdminOrderList";
import React from "react";

const AdminOrdersPage: React.FC = () => {
  return (
    <>
      <AdminOrderList pageSize={6}/>
    </>
  );
};

export default AdminOrdersPage;
