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
  Stack,
  Typography,
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

  const details = product.details.split(".");
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
            backgroundColor: "#145607",
            color: "white",
            fontFamily: "Poppins",
            fontWeight: 400,
            fontStyle: "normal",
          }}
        >
          Product Catalogue
        </DialogTitle>

        <DialogContent>
          <Stack
            sx={{ width: "100%", padding: 2 }}
            direction={"row"}
            spacing={2}
          >
            <Stack sx={{ width: "30%" }} direction={"column"}>
              {product.images != "" ? (
                <img
                  src={product.images}
                  alt="Product"
                  width={"100%"}
                  height={300}
                />
              ) : (
                <Box
                  sx={{
                    backgroundColor: "#D6F7A9",
                    width: "100%",
                    height: 300,
                  }}
                ></Box>
              )}
            </Stack>
            <Stack sx={{ width: "70%" }} direction={"column"} spacing={2}>
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: "700",
                  fontStyle: "bold",
                  fontSize: 24,
                  textColor: "black",
                }}
              >
                {product.productName.toLocaleUpperCase()}
              </Typography>
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: "400",
                  textColor: "black",
                  fontStyle: "normal",
                  fontSize: 18,
                }}
              >
                {product.description}
              </Typography>
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: "700",
                  textColor: "black",
                  fontStyle: "bold",
                  fontSize: 18,
                }}
              >
                More Details
              </Typography>
              {
                <ul className="details-list">
                  {details.map((data, index) => (
                    <li key={index}>
                      <Typography
                        sx={{
                          fontFamily: "Poppins",
                          fontWeight: "400",
                          textColor: "black",
                          fontStyle: "normal",
                          fontSize: 18,
                        }}
                      >
                        {data}
                      </Typography>
                    </li>
                  ))}
                </ul>
              }
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: "700",
                  textColor: "black",
                  fontStyle: "bold",
                  fontSize: 18,
                }}
              >
                Quantity
              </Typography>
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: "400",
                  textColor: "black",
                  fontStyle: "normal",
                  fontSize: 18,
                }}
              >
                {product.quantity}
              </Typography>

              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: "700",
                  textColor: "black",
                  fontStyle: "bold",
                  fontSize: 18,
                }}
              >
                Cost
              </Typography>
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: "400",
                  textColor: "black",
                  fontStyle: "normal",
                  fontSize: 24,
                }}
              >
                ₱{product.cost}
              </Typography>
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: "700",
                  textColor: "black",
                  fontStyle: "bold",
                  fontSize: 24,
                }}
              >
                Price
              </Typography>
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: "400",
                  textColor: "black",
                  fontStyle: "normal",
                  fontSize: 24,
                }}
              >
                ₱{product.price}
              </Typography>
            </Stack>
          </Stack>
        </DialogContent>
        <Divider />

        <DialogActions>
          <Button variant="contained" color="error" onClick={handleClose}>
            Close
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default ViewProductDialog;
