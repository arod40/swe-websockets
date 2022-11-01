import { type UserStatus } from "./user";

interface Message {
  content: string;
  from: string;
}

interface BackendMessage extends Message {
  status: UserStatus;
  type: "MESSAGE" | "STATUS_CHANGE";
}

export { type BackendMessage, type Message };
