import { useState } from "react";
import Dialog from "@mui/material/Dialog";

import Slide from "@mui/material/Slide";
import { TransitionProps } from "@mui/material/transitions";

import {
  Box,
  Button,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Divider,

} from "@mui/material";
import { Products } from "../models/Products";
interface ViewProductDialogProps {
  product: Products;
}

const ViewProductDialog: React.FunctionComponent<ViewProductDialogProps> = (
  props
) => {
  const { product } = props;
  const [open, setOpen] = useState(false);
  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };
  return (
    <div>
      <Button variant={"contained"} color={"success"} onClick={handleClickOpen}>
        View
      </Button>
      <Dialog
        open={open}
        onClose={handleClose}
        maxWidth={"lg"}
        fullWidth
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle
          id="alert-dialog-title"
          sx={{
            backgroundColor: "#91C6EE",
            color: "black",
            fontFamily: "Poppins",
            fontWeight: 400,
            fontStyle: "normal",
          }}
        >
          Product Catalogue
        </DialogTitle>

        <DialogContent>
          <DialogContentText>Dito ka magcode!</DialogContentText>
        </DialogContent>
        <Divider />
        <DialogActions></DialogActions>
      </Dialog>
    </div>
  );
};

export default ViewProductDialog;
