import React, { useState } from "react";
import "./App.css";
import ChatRoom from "./components/ChatRoom";

function App() {
  const [username, setUsername] = useState("");
  const [registered, setRegistered] = useState(false);

  const handleUsernameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setUsername(event.target.value);
  };

  const handleRegister = () => {
    setRegistered(true);
  };

  return registered ? (
    <ChatRoom username={username}></ChatRoom>
  ) : (
    <div>
      <input value={username} onChange={handleUsernameChange} />
      <button onClick={handleRegister}>Login</button>
    </div>
  );
}

export default App;
