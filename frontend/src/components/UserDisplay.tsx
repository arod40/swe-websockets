import React from "react";
import { UserStatus, type User } from "./user";

import ListItem from "@mui/material/ListItem";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import DoDisturbOnIcon from "@mui/icons-material/DoDisturbOn";
import DoDisturbOffIcon from "@mui/icons-material/DoDisturbOff";
import NoAccountsIcon from "@mui/icons-material/NoAccounts";

interface UserDisplayProps {
  user: User;
}

const UserDisplay = <T extends UserDisplayProps>(props: T) => {
  const { user } = props;
  return (
    <ListItem disablePadding>
      <ListItemIcon>
        {user.status === "DND" ? (
          <DoDisturbOnIcon></DoDisturbOnIcon>
        ) : (
          <DoDisturbOffIcon></DoDisturbOffIcon>
        )}
      </ListItemIcon>
      <ListItemText primary={user.username} />
    </ListItem>
  );
};

export default UserDisplay;
