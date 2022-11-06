import { Container, Box, Stack, Typography } from "@mui/material";
import AddInventoryPage from "../dialog/AddInventory";

import React, { useEffect, useState } from "react";

import Divider from "@mui/material/Divider";

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
            flexGrow: 1,
            alignItems: "center",
            display: "flex",
            justifyContent: "space-between",
          }}
        >
          <Typography
            sx={{
              fontFamily: "Poppins",
              fontWeight: 400,
              fontSize: 30,
              fontStyle: "normal",
            }}
          >
            Inventory
          </Typography>
          <AddInventoryPage />
        </Box>
        <Divider />
        <InventoryTable />
      </Stack>
    </Box>
  );
};

export default InventoryPage;
