import { Container, Box, Stack } from "@mui/material";
import AddInventoryPage from "../dialog/AddInventory";
import { experimentalStyled as styled } from "@mui/material/styles";
import React, { useEffect, useState } from "react";
import Paper from "@mui/material/Paper";
import Grid from "@mui/material/Grid";
import ProductCard from "../components/ProductCard";
import {
  collection,
  doc,
  onSnapshot,
  orderBy,
  query,
  where,
} from "firebase/firestore";
import { useAuth } from "../auth/AuthContext";
import { firestore } from "../config/config";
import { PRODUCTS_TABLE } from "../utils/Constants";
import InventoryTable from "./InventoryTable";
interface InventoryPageProps {}

const InventoryPage: React.FunctionComponent<InventoryPageProps> = () => {
  return (
    <Box
      sx={{
        width: "100%",
        padding: 2,
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <Stack direction={"column"} spacing={2} sx={{ width: "100%" }}>
        <Box
          sx={{
            alignItems: "end",
            display: "flex",
            justifyContent: "end",
          }}
        >
          <AddInventoryPage />
        </Box>
        <InventoryTable />
      </Stack>
    </Box>
  );
};

export default InventoryPage;
