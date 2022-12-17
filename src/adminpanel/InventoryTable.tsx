import React, { useEffect, useState } from "react";
import { styled } from "@mui/material/styles";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import {
  collection,
  onSnapshot,
  orderBy,
  query,
  where,
} from "firebase/firestore";
import { firestore } from "../config/config";
import { PRODUCTS_TABLE } from "../utils/Constants";
import { useAuth } from "../auth/AuthContext";
import { Button, Stack } from "@mui/material";
import DeleteProductDialog from "../dialog/DeleteProduct";
import UpdateProductDialog from "../dialog/UpdateProduct";
import ViewProductDialog from "../dialog/ViewProduct";
import { Products } from "../models/Products";

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: "#145607",
    color: theme.palette.common.white,
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 18,
    fontFamily: "Poppins",
    fontWeight: 400,
    fontStyle: "normal",
  },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  "&:nth-of-type(odd)": {
    backgroundColor: theme.palette.action.hover,
  },
  // hide last border
  "&:last-child td, &:last-child th": {
    border: 0,
  },
}));

interface InventoryTableProps {
  product: any[];
}

const InventoryTable: React.FunctionComponent<InventoryTableProps> = (
  props
) => {
  const { product } = props;
  const { currentUser } = useAuth();
  return (
    <TableContainer component={Paper} sx={{ maxHeight: 800 }}>
      <Table sx={{ minWidth: 700 }} stickyHeader aria-label="Inventory table">
        <TableHead>
          <TableRow>
            <StyledTableCell>Name</StyledTableCell>
            <StyledTableCell align="left">Code</StyledTableCell>
            <StyledTableCell align="right">Quantity</StyledTableCell>
            <StyledTableCell align="right">Cost</StyledTableCell>
            <StyledTableCell align="right">Price</StyledTableCell>
            <StyledTableCell align="right">Category</StyledTableCell>
            <StyledTableCell align="right">Weight</StyledTableCell>
            <StyledTableCell align="right">Expiration</StyledTableCell>
            <StyledTableCell align="center">Actions</StyledTableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {product.length != 0 ? (
            product.map((row) => (
              <StyledTableRow key={row.id}>
                <StyledTableCell align="left">
                  {row.productName}
                </StyledTableCell>
                <StyledTableCell align="left">{row.code}</StyledTableCell>
                <StyledTableCell align="right">{row.quantity}</StyledTableCell>
                <StyledTableCell align="right">{row.cost}</StyledTableCell>
                <StyledTableCell align="right">{row.price}</StyledTableCell>
                <StyledTableCell align="right">{row.category}</StyledTableCell>
                <StyledTableCell align="right">{row.weight}</StyledTableCell>

                <StyledTableCell align="right">
                  {row.expiration}
                </StyledTableCell>
                <StyledTableCell align="right">
                  <Stack
                    direction={"row"}
                    spacing={1}
                    sx={{
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                    }}
                  >
                    <ViewProductDialog product={row} />
                    <UpdateProductDialog
                      product={row}
                      currentUser={currentUser.uid}
                    />
                    <DeleteProductDialog productID={row.id} />
                  </Stack>
                </StyledTableCell>
              </StyledTableRow>
            ))
          ) : (
            <StyledTableRow>
              <StyledTableCell align="center" colSpan={9}>
                No products yet!
              </StyledTableCell>
            </StyledTableRow>
          )}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default InventoryTable;
