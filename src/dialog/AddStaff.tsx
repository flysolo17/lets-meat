import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  LinearProgress,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import { useState } from "react";
import { useAuth } from "../auth/AuthContext";
import { firestore, storage } from "../config/config";
import { Staff } from "../models/Staff";
import {
  PRODUCT_STORAGE,
  STAFF_STORAGE,
  STAFF_TABLE,
} from "../utils/Constants";
import { v4 as uuidv4 } from "uuid";
import { getDownloadURL, ref, uploadBytes } from "firebase/storage";
import { addDoc, collection, doc } from "firebase/firestore";
import { getTimestamp } from "../utils/Constants";
interface AddStaffDialogProps {}

const AddStaffDialog: React.FunctionComponent<AddStaffDialogProps> = (
  props
) => {
  const [open, setOpen] = useState(false);
  const { currentUser } = useAuth();
  const [staffProfile, setStaffProfile] = useState<any>(null);
  const [loading, setLoading] = useState(false);
  const [staff, setStaff] = useState<Staff>({
    profile: "",
    displayName: "",
    fullname: "",
    contactNumber: "",
    pin: "0000",
    employedAt: 0,
  });
  const handleClickOpen = () => {
    setOpen(true);
  };
  const handleClose = () => {
    setStaff({
      profile: "",
      displayName: "",
      fullname: "",
      contactNumber: "",
      pin: "0000",
      employedAt: 0,
    });
    setOpen(false);
  };
  const onImageChange = (event: any) => {
    setStaffProfile(event.target.files[0]);
    if (event.target.files && event.target.files[0]) {
      let reader = new FileReader();
      reader.onload = (e: any) => {
        setStaff({ ...staff, profile: e.target.result });
      };
      reader.readAsDataURL(event.target.files[0]);
    }
  };

  function uploadWithImage(file: any) {
    setLoading(true);
    if (staffProfile == null) return;
    const fireRef = ref(
      storage,
      `${currentUser.uid!}/${STAFF_STORAGE}/${uuidv4()}`
    );
    uploadBytes(fireRef, file)
      .then((snapshot) => {
        getDownloadURL(snapshot.ref).then((url) => {
          console.log(url);
          addDoc(collection(firestore, STAFF_TABLE), {
            ...staff,
            profile: url,
            employedAt: getTimestamp(),
          })
            .then(() => {
              console.log("New Staff added!");
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
  const saveStaff = () => {
    if (staffProfile != null) {
      uploadWithImage(staffProfile);
      return;
    }
    addDoc(collection(firestore, STAFF_TABLE), {
      ...staff,
      employedAt: getTimestamp(),
    })
      .then(() => {
        console.log("New Staff added!");
      })
      .catch((error) => {
        console.log(error);
      })
      .finally(() => {
        setLoading(false);
        handleClose();
      });
  };
  return (
    <div>
      <Button variant={"contained"} color={"success"} onClick={handleClickOpen}>
        Create Staff
      </Button>
      <Dialog
        open={open}
        onClose={handleClose}
        maxWidth={"lg"}
        fullWidth
        aria-labelledby="Create Staff Account"
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
          Create Staff Account
        </DialogTitle>
        {loading && <LinearProgress />}
        <DialogContent>
          <Stack direction={"row"} spacing={1} padding={2}>
            <Stack direction={"column"} spacing={0} sx={{ width: "30%" }}>
              {staff.profile != "" ? (
                <img
                  src={staff.profile}
                  alt="Staff Profile"
                  width={"100%"}
                  height={200}
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
              <Button
                variant="contained"
                component="label"
                color={"success"}
                sx={{ backgroundColor: "#EC4A0A" }}
              >
                Add Profile
                <input
                  hidden
                  accept="image/*"
                  multiple
                  type="file"
                  onChange={onImageChange}
                />
              </Button>
            </Stack>
            <Stack direction={"column"} spacing={0} sx={{ width: "70%" }}>
              <TextField
                fullWidth={false}
                sx={{ width: "30%" }}
                label={"Display name"}
                value={staff.displayName}
                onChange={(e) =>
                  setStaff({ ...staff, displayName: e.target.value })
                }
                helperText={"*For display purposes only"}
                color={"success"}
              />
              <Stack direction={"column"} padding={5} spacing={2}>
                <TextField
                  label={"Fullname"}
                  color={"success"}
                  value={staff.fullname}
                  onChange={(e) =>
                    setStaff({ ...staff, fullname: e.target.value })
                  }
                />
                <TextField
                  label={"Contact #"}
                  color={"success"}
                  value={staff.contactNumber}
                  onChange={(e) =>
                    setStaff({ ...staff, contactNumber: e.target.value })
                  }
                  helperText={"*For the communication of the owner and staff"}
                />
                <TextField
                  label={"Pin"}
                  color={"success"}
                  helperText={
                    "*For the protect of the staff. Enter 4 digit pin"
                  }
                  value={staff.pin}
                  inputProps={{
                    maxLength: 4,
                  }}
                  onChange={(e) => {
                    setStaff({ ...staff, pin: e.target.value });
                  }}
                />
              </Stack>
            </Stack>
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button color={"success"} variant={"contained"} onClick={saveStaff}>
            Create
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default AddStaffDialog;
