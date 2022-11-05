import React, { useEffect, useState } from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";

import Slide from "@mui/material/Slide";
import { TransitionProps } from "@mui/material/transitions";

import {
  Box,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Divider,
  LinearProgress,
  Stack,
  TextField,
} from "@mui/material";

import { Products } from "../models/Products";
import { useAuth } from "../auth/AuthContext";
import { v4 as uuidv4 } from "uuid";
import { firestore, storage } from "../config/config";
import { getDownloadURL, ref, uploadBytes } from "firebase/storage";
import { PRODUCTS_TABLE, PRODUCT_STORAGE } from "../utils/Constants";
import { doc, Firestore, setDoc, updateDoc } from "firebase/firestore";



interface UpdateProductDialogProps {
  product: Products;
  currentUser: string;
}

const UpdateProductDialog: React.FunctionComponent<UpdateProductDialogProps> = (
  props
) => {
  const { product, currentUser } = props;
  const [updatedProduct, setUpdatedProduct] = useState({
    images: product.images,
    productName: product.productName,
    cost: product.cost,
    price: product.price,
    quantity: product.quantity,
    description: product.description,
    details: product.details,
  });
  const [loading, setLoading] = useState(false);
  const [forUpload, setForUpload] = useState("");
  const [open, setOpen] = useState(false);
  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const onImageChange = (event: any) => {
    setForUpload(event.target.files[0]);
    if (event.target.files && event.target.files[0]) {
      let reader = new FileReader();
      reader.onload = (e: any) => {
        setUpdatedProduct({ ...updatedProduct, images: e.target.result });
      };
      reader.readAsDataURL(event.target.files[0]);
    }
  };
  const updateWithImage = (file: any) => {
    if (file == null && product.code != "") return;
    setLoading(true);
    const fireRef = ref(
      storage,
      `${currentUser}/${PRODUCT_STORAGE}/${uuidv4()}`
    );
    uploadBytes(fireRef, file)
      .then((snapshot) => {
        getDownloadURL(snapshot.ref).then((url) => {
          console.log(url);
          updateDoc(doc(firestore, PRODUCTS_TABLE, product.code), {
            images: url,
            productName: updatedProduct.productName,
            cost: updatedProduct.cost,
            price: updatedProduct.price,
            quantity: updatedProduct.quantity,
            description: updatedProduct.description,
            details: updatedProduct.details,
          })
            .then(() => {
              console.log(" item Updated!");
            })
            .catch((error) => {
              console.log(error);
            })
            .finally(() => {
              setLoading(false);
              handleClose();
            });
        });
      })
      .catch((error) => {
        console.log(error);
      })
      .finally(() => {
        setLoading(false);
      });
  };
  const saveUpdate = () => {
    setLoading(true);
    if (product.code != "") {
      updateDoc(doc(firestore, PRODUCTS_TABLE, product.code), {
        productName: updatedProduct.productName,
        cost: updatedProduct.cost,
        price: updatedProduct.price,
        quantity: updatedProduct.quantity,
        description: updatedProduct.description,
        details: updatedProduct.details,
      })
        .then(() => {
          console.log("item updated!");
        })
        .catch((error) => {
          console.log(error);
        })
        .finally(() => {
          setLoading(false);
          handleClose();
        });
    } else {
      setLoading(false);
    }
  };
  const updateProduct = (e: any) => {
    e.preventDefault();
    if (forUpload != "") {
      updateWithImage(forUpload);
      return;
    }
    saveUpdate();
  };
  return (
    <div>
      <Button variant={"contained"} onClick={handleClickOpen}>
        Edit
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
          Update Product
        </DialogTitle>
        {loading && <LinearProgress />}
        <DialogContent>
          <Stack
            sx={{ width: "100%", padding: 2 }}
            direction={"row"}
            spacing={2}
          >
            <Stack sx={{ width: "30%" }} direction={"column"}>
              {updatedProduct.images != "" ? (
                <img
                  src={updatedProduct.images}
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
              <Button variant="contained" component="label">
                Change Image
                <input
                  hidden
                  accept="image/*"
                  multiple
                  type="file"
                  onChange={onImageChange}
                />
              </Button>
            </Stack>
            <Stack sx={{ width: "70%" }} direction={"column"} spacing={2}>
              <TextField
                label={"Product Code"}
                variant={"filled"}    
                value={product.code}
              />
              <TextField
                label={"Product name"}
                variant={"filled"}
                value={updatedProduct.productName}
                onChange={(e) =>
                  setUpdatedProduct({
                    ...updatedProduct,
                    productName: e.target.value,
                  })
                }
              />
              <TextField
                id="outlined-multiline-static"
                label="Description"
                multiline
                rows={4}
                variant={"filled"}
                value={updatedProduct.description}
                onChange={(e) =>
                  setUpdatedProduct({
                    ...updatedProduct,
                    description: e.target.value,
                  })
                }
              />
              <TextField
                id="outlined-multiline-static"
                label="Details"
                multiline
                rows={4}
                variant={"filled"}
                value={updatedProduct.details}
                onChange={(e) =>
                  setUpdatedProduct({
                    ...updatedProduct,
                    details: e.target.value,
                  })
                }
              />
              <Stack direction={"row"} spacing={1} sx={{ width: "100%" }}>
                <TextField
                  label={"Quantity"}
                  variant={"filled"}
                  fullWidth
                  type={"number"}
                  inputProps={{ inputMode: "numeric", pattern: "[0-9]*" }}
                  value={updatedProduct.quantity}
                  onChange={(e) =>
                    setUpdatedProduct({
                      ...updatedProduct,
                      quantity: parseInt(e.target.value),
                    })
                  }
                />
                <TextField
                  label={"Cost"}
                  variant={"filled"}
                  fullWidth
                  inputProps={{ inputMode: "numeric", pattern: "[0-9]*" }}
                  required
                  type={"number"}
                  value={updatedProduct.cost}
                  onChange={(e) =>
                    setUpdatedProduct({
                      ...updatedProduct,
                      cost: parseInt(e.target.value),
                    })
                  }
                />
                <TextField
                  label={"Price"}
                  variant={"filled"}
                  type={"number"}
                  required
                  fullWidth
                  inputProps={{ inputMode: "numeric", pattern: "[0-9]*" }}
                  value={updatedProduct.price}
                  onChange={(e) =>
                    setUpdatedProduct({
                      ...updatedProduct,
                      price: parseInt(e.target.value),
                    })
                  }
                />
              </Stack>
            </Stack>
          </Stack>
        </DialogContent>
        <Divider />
        <DialogActions>
          <Button onClick={handleClose} autoFocus>
            CANCEL
          </Button>
          <Button onClick={updateProduct}>Update</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default UpdateProductDialog;
