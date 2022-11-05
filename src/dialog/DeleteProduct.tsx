import * as React from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import { deleteDoc, doc } from "firebase/firestore";
import { firestore } from "../config/config";
import { PRODUCTS_TABLE } from "../utils/Constants";

interface DeleteProductDialogProps {
  productID: string;
}

const DeleteProductDialog: React.FunctionComponent<DeleteProductDialogProps> = (
  props
) => {
  const { productID } = props;
  const [open, setOpen] = React.useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };
  const deleteProduct = () => {
    deleteDoc(doc(firestore, PRODUCTS_TABLE, productID))
      .then(() => {
        console.log("product deleted");
      })
      .catch((error) => {
        console.error(error);
      })
      .finally(() => {
        handleClose();
      });
  };
  return (
    <div>
      <Button color={"error"} variant={"contained"} onClick={handleClickOpen}>
        Delete
      </Button>
      <Dialog
        open={open}
        onClose={handleClose}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle
          id="alert-dialog-title"
          sx={{
            backgroundColor: "#FF7765",
            color: "black",
            fontFamily: "Poppins",
            fontWeight: 400,
            fontStyle: "normal",
          }}
        >
          Delete Product
        </DialogTitle>
        <DialogContent>
          <DialogContentText
            id="alert-dialog-description"
            sx={{
              fontFamily: "Poppins",
              fontWeight: 400,
              fontStyle: "normal",
              padding: 2,
              fontSize: 20,
              color: "black",
              textAlign: "center",
            }}
          >
            Are you sure you want to delete this product?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} autoFocus>
            CANCEL
          </Button>
          <Button onClick={deleteProduct}>YES</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default DeleteProductDialog;
