import HomeIcon from "@mui/icons-material/Home";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import InfoIcon from "@mui/icons-material/Info";
import InventoryIcon from "@mui/icons-material/Inventory";
import DashboardIcon from "@mui/icons-material/Dashboard";
import GroupsIcon from "@mui/icons-material/Groups";

export const staffNavData = [
  {
    title: "Home",
    path: "/cashier/home",
    icon: <DashboardIcon />,
  },
  {
    title: "Cash Drawer",
    path: "/cashier/drawer",
    icon: <InventoryIcon />,
  },
  {
    title: "Staff Account",
    path: "/cashier/account",
    icon: <AccountCircleIcon />,
  },
];
