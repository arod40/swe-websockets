import React from "react";
import { type Message } from "./message";

interface MessageDisplayProps {
  message: Message;
}

const MessageDisplay = <T extends MessageDisplayProps>(props: T) => {
  const { message } = props;
  return <div>{message.from} - {message.content}</div>;
};

export default MessageDisplay;
