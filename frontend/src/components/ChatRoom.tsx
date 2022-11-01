import React, { useState } from "react";
import DraftArea from "./DraftArea";
import useWebSocket from "react-use-websocket";
import { type BackendMessage, type Message } from "./message";
import MessageDisplay from "./MessageDisplay";
import { type User } from "./user";
import { Grid } from "@mui/material";
import List from "@mui/material/List";
import UserDisplay from "./UserDisplay";
import Switch from "@mui/material/Switch";

const SOCKET_BASE_URL = "ws://localhost:8025/chat/";

interface ChatRoomProps {
  username: string;
}

export default function ChatRoom(props: ChatRoomProps) {
  const [messages, setMessages] = useState<Message[]>([]);
  const [users, setUsers] = useState<Record<string, User>>({});
  const [dnd, setDnd] = useState(false);
  const { sendMessage } = useWebSocket(SOCKET_BASE_URL + props.username, {
    onMessage: (event) => {
      const message: BackendMessage = JSON.parse(event.data);
      if (message.type === "MESSAGE") {
        setMessages([...messages, message]);
      } else if (message.type === "STATUS_CHANGE") {
        setUsers({
          ...users,
          [message.from]: {
            username: message.from,
            status: message.status!,
          },
        });
      }
      else if (message.type === "USER_LIST") {
        console.log(message.users);
        setUsers(message.users!.reduce((acc, user) => {
          acc[user.username] = user;
          return acc;
        }, {} as Record<string, User>));
      }
    },
  });

  const handleSend = (message: string) => {
    const msg: BackendMessage = {
      from: props.username,
      type: "MESSAGE",
      content: message,
      status: "ONLINE",
    };

    sendMessage(JSON.stringify(msg));
  };

  const handleDndChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setDnd(event.target.checked);
    const msg: BackendMessage = {
      from: props.username,
      type: "STATUS_CHANGE",
      content: "",
      status: event.target.checked ? "DND" : "ONLINE",
    };

    sendMessage(JSON.stringify(msg));
  };

  return (
    <div>
      <Grid container spacing={2}>
        <Grid item xs={2}>
          <List>
            {Object.values(users).map((user) => (
              <UserDisplay
                key={"user_" + user.username}
                user={user}
              ></UserDisplay>
            ))}
          </List>
        </Grid>
        <Grid item xs={10}>
          <div>
            {messages.map((message, i) => (
              <MessageDisplay
                key={"mDisplay" + i}
                message={message}
              ></MessageDisplay>
            ))}
          </div>
          <DraftArea onSend={handleSend}></DraftArea>
          <Switch
            edge="end"
            onChange={handleDndChange}
            checked={dnd}
          />
        </Grid>
      </Grid>
    </div>
  );
}
