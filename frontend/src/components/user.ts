type UserStatus = "DND" | "ONLINE" | "OFFLINE";

interface User {
  username: string;
  status: UserStatus;
}

export { type User, type UserStatus };
