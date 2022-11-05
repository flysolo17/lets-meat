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

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: "#145607",
    color: theme.palette.common.white,
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 16,
    fontFamily: "Poppins",
    fontWeight: 700,
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

function createData(
  name: string,
  calories: number,
  fat: number,
  carbs: number,
  protein: number
) {
  return { name, calories, fat, carbs, protein };
}

const rows = [
  createData("Frozen yoghurt", 159, 6.0, 24, 4.0),
  createData("Ice cream sandwich", 237, 9.0, 37, 4.3),
  createData("Eclair", 262, 16.0, 24, 6.0),
  createData("Cupcake", 305, 3.7, 67, 4.3),
  createData("Gingerbread", 356, 16.0, 49, 3.9),
  createData("Gingerbread", 356, 16.0, 49, 3.9),
  createData("Gingerbread", 356, 16.0, 49, 3.9),
  createData("Gingerbread", 356, 16.0, 49, 3.9),
  createData("Gingerbread", 356, 16.0, 49, 3.9),
  createData("Gingerbread", 356, 16.0, 49, 3.9),
  createData("Gingerbread", 356, 16.0, 49, 3.9),
  createData("Gingerbread", 356, 16.0, 49, 3.9),
  createData("Gingerbread", 356, 16.0, 49, 3.9),
  createData("Gingerbread", 356, 16.0, 49, 3.9),
  createData("Gingerbread", 356, 16.0, 49, 3.9),
  createData("Gingerbread", 356, 16.0, 49, 3.9),
];
interface InventoryTableProps {}

const InventoryTable: React.FunctionComponent<InventoryTableProps> = (
  props
) => {
  const [product, setProduct] = useState<any[]>([]);
  const { currentUser } = useAuth();
  useEffect(() => {
    if (currentUser != null) {
      const PRODUCTS_REF = collection(firestore, PRODUCTS_TABLE);
      const PRODUCTS_QUERY = query(
        PRODUCTS_REF,
        where("userID", "==", currentUser.uid),
        orderBy("createdAt", "desc")
      );
      const unsub = onSnapshot(PRODUCTS_QUERY, (snapshot) => {
        let data: any = [];
        snapshot.forEach((doc) => {
          data.push({ ...doc.data(), id: doc.id });
        });
        setProduct(data);
        console.log(data);
      });
      return () => unsub();
    }
  }, []);

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
                    <Button color="success" variant={"contained"}>
                      View
                    </Button>
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
              <StyledTableCell align="center" colSpan={5}>
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
