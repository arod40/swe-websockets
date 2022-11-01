import React, { useState } from "react";

interface DraftAreaProps {
  onSend: (message: string) => void;
}

const DraftArea = (props: DraftAreaProps) => {
  const [draft, setDraft] = useState("");

  const handleSend = () => {
    props.onSend(draft);
    setDraft("");
  };

  return (
    <div>
      <textarea value={draft} onChange={(e) => setDraft(e.target.value)} />
      <button onClick={handleSend}>Send</button>
    </div>
  );
};

export default DraftArea;
