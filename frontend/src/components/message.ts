import { type User, type UserStatus } from "./user";

interface Message {
  content: string;
  from: string;
}

interface BackendMessage extends Message {
  status?: UserStatus;
  type: "MESSAGE" | "STATUS_CHANGE" | "USER_LIST";
  users?: User[];
}

export { type BackendMessage, type Message };
