import React, { useState } from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import Box from "@mui/material/Box";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import Typography from "@mui/material/Typography";
import CloseIcon from "@mui/icons-material/Close";
import Slide from "@mui/material/Slide";
import { TransitionProps } from "@mui/material/transitions";
import Add from "@mui/icons-material/Add";
import AddImage from "../images/addimage.png";
import {
  DialogContent,
  DialogContentText,
  LinearProgress,
  Paper,
  Stack,
  TextField,
} from "@mui/material";
import { getDownloadURL, ref, uploadBytes } from "firebase/storage";
import { firestore, storage } from "../config/config";
import {
  getTimestamp,
  PRODUCTS_TABLE,
  PRODUCT_STORAGE,
} from "../utils/Constants";
import { addDoc, collection, doc } from "firebase/firestore";
import { Products } from "../models/Products";
import { useAuth } from "../auth/AuthContext";
import { v4 as uuidv4 } from "uuid";
const Transition = React.forwardRef(function Transition(
  props: TransitionProps & {
    children: React.ReactElement;
  },
  ref: React.Ref<unknown>
) {
  return <Slide direction="up" ref={ref} {...props} />;
});

interface AddInventoryPageProps {}

const AddInventoryPage: React.FunctionComponent<AddInventoryPageProps> = () => {
  const [open, setOpen] = useState(false);
  const { currentUser } = useAuth();
  const [image, setImage] = useState(null);
  const [forUpload, setForUpload] = useState("");
  const [loading, setLoading] = useState(false);
  const [product, setProduct] = useState<Products>({
    userID: currentUser.uid,
    images: "",
    productName: "",
    cost: 0,
    price: 0,
    quantity: 0,
    description: "",
    details: "",
    isAvailable: true,
    createdAt: getTimestamp(),
  });
  const onImageChange = (event: any) => {
    setForUpload(event.target.files[0]);
    if (event.target.files && event.target.files[0]) {
      let reader = new FileReader();
      reader.onload = (e: any) => {
        setImage(e.target.result);
      };
      reader.readAsDataURL(event.target.files[0]);
    }
  };
  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setProduct({
      userID: currentUser.uid,
      images: "",
      productName: "",
      cost: 0,
      price: 0,
      quantity: 0,
      description: "",
      details: "",
      isAvailable: true,
      createdAt: getTimestamp(),
    });
    setForUpload("");
    setImage(null);
    setOpen(false);
  };
  function uploadFile(file: any) {
    setLoading(true);
    if (file == null) return;
    const fireRef = ref(
      storage,
      `${currentUser.uid!}/${PRODUCT_STORAGE}/${uuidv4()}`
    );
    uploadBytes(fireRef, file)
      .then((snapshot) => {
        getDownloadURL(snapshot.ref).then((url) => {
          console.log(url);
          addDoc(collection(firestore, PRODUCTS_TABLE), {
            ...product,
            images: url,
          })
            .then(() => {
              console.log("New item added!");
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
  }
  const uploadItem = () => {
    setLoading(true);
    addDoc(collection(firestore, PRODUCTS_TABLE), product)
      .then(() => {
        console.log("New item added!");
      })
      .catch((error) => {
        console.log(error);
      })
      .finally(() => {
        setLoading(false);
        handleClose();
      });
  };

  const addItemInventory = (e: any) => {
    e.preventDefault();
    if (image != null) {
      uploadFile(forUpload);
      return;
    }
    uploadItem();
  };

  return (
    <div>
      <Button
        variant="contained"
        onClick={handleClickOpen}
        startIcon={<Add />}
        color={"success"}
      >
        Add Item
      </Button>
      <Dialog
        fullScreen
        open={open}
        onClose={handleClose}
        TransitionComponent={Transition}
      >
        <AppBar sx={{ position: "relative" }}>
          <Toolbar sx={{ backgroundColor: "#004d40" }}>
            <IconButton
              edge="start"
              color="inherit"
              onClick={handleClose}
              aria-label="close"
            >
              <CloseIcon />
            </IconButton>
            <Typography sx={{ ml: 2, flex: 1 }} variant="h6" component="div">
              Add Item
            </Typography>
          </Toolbar>
          {loading && <LinearProgress color="success" />}
        </AppBar>

        <DialogContent
          sx={{
            display: "flex",
            alignItems: "center",
            backgroundColor: "#e0f2f1",
            alignItem: "center",
            justifyContent: "center",
            padding: 2,
          }}
        >
          <Paper
            sx={{
              height: "80vh",
              width: "70%",
              display: "flex",
            }}
            elevation={1}
          >
            <Stack
              direction={"column"}
              spacing={5}
              sx={{ width: "40%", padding: 2 }}
            >
              <Typography
                sx={{
                  fontFamily: "Poppins",
                  fontWeight: 400,
                  fontSize: 40,
                  margin: 2,
                  fontStyle: "bold",
                }}
              >
                Product Catalogue
              </Typography>

              {image != null ? (
                <img src={image} alt="Product" width={"100%"} height={300} />
              ) : (
                <Box
                  sx={{
                    backgroundColor: "#D6F7A9",
                    width: "100%",
                    height: 300,
                  }}
                ></Box>
              )}

              <Button
                variant="contained"
                component="label"
                color={"success"}
                sx={{ backgroundColor: "#EC4A0A" }}
              >
                add image
                <input
                  hidden
                  accept="image/*"
                  multiple
                  type="file"
                  onChange={onImageChange}
                />
              </Button>
            </Stack>
            <Stack
              direction={"column"}
              spacing={2}
              sx={{ width: "60%", padding: 5 }}
            >
              <TextField
                label={"Product name"}
                variant={"filled"}
                value={product.productName}
                onChange={(e) =>
                  setProduct({ ...product, productName: e.target.value })
                }
              />
              <TextField
                id="outlined-multiline-static"
                label="Descriptiom"
                multiline
                rows={6}
                variant={"filled"}
                value={product.description}
                onChange={(e) =>
                  setProduct({ ...product, description: e.target.value })
                }
              />
              <TextField
                id="outlined-multiline-static"
                label="Details"
                multiline
                rows={6}
                variant={"filled"}
                value={product.details}
                onChange={(e) =>
                  setProduct({ ...product, details: e.target.value })
                }
              />
              <TextField
                label={"Quantity"}
                variant={"filled"}
                type={"number"}
                value={product.quantity}
                onChange={(e) =>
                  setProduct({ ...product, quantity: parseInt(e.target.value) })
                }
              />
              <TextField
                label={"Cost"}
                variant={"filled"}
                type={"number"}
                value={product.cost}
                onChange={(e) =>
                  setProduct({ ...product, cost: parseInt(e.target.value) })
                }
              />
              <TextField
                label={"Price"}
                variant={"filled"}
                type={"number"}
                value={product.price}
                onChange={(e) =>
                  setProduct({ ...product, price: parseInt(e.target.value) })
                }
              />
              <Button
                autoFocus
                color="success"
                onClick={addItemInventory}
                variant={"contained"}
                sx={{
                  padding: 2,
                  borderRadius: 10,
                  marginTop: 2,
                }}
              >
                save
              </Button>
            </Stack>
          </Paper>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default AddInventoryPage;
