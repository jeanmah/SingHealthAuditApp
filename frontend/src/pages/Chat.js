import { FormGroup } from "@material-ui/core";
import { Typography, Button } from "@material-ui/core";
import React, { useState, useEffect, useContext } from "react";
import { useParams } from "react-router-dom";

import { Context } from '../Context';
import useStyles from "../styles";

function Chat() {
  const { chatId } = useParams(); // typeof chatId: String
  const [chatEntriesState, setChatEntriesState] = useState([]);
  const { getChatEntriesOfUser, postChatEntry } = useContext(Context);
  const styles = useStyles();
  
  useEffect(() => {
    async function getResponse() {
      try{
        await getChatEntriesOfUser(chatId).then((response) => {
          //console.log("Chat: allChatsOfUser: " + response.data);
          setChatEntriesState(response.data);
          //console.log("state: " + chatEntriesState);
        })
      } catch {
        console.log("Failed to retrive allChatsOfUser");
      }
    };
    getResponse();
  }, []);

  function handleClick() {
    let parentChatId = 3;
    let subject = "Testing Post Chat Entry";
    let messageBody = "Test, test, test...";
    let attachments = "";
    console.log("This is calling postNewChatEntry");
    //postChatEntry();
  }

  return (
    <main>
      <Typography variant="h3" align='center'>Chat</Typography>
      <Typography align='center'>Chat ID: {chatId}</Typography>
      <br />
      {chatEntriesState.map((entry, index) => {
        return (
          <React.Fragment key={index}>
            <FormGroup>
              <Typography>Chat Entry ID: {entry.chat_entry_id}</Typography>
              <Typography>Subject:{entry.subject}</Typography>
              <Typography>Sender ID: {entry.sender_id}</Typography>
              <Typography>Time: {entry.time}</Typography>
              <Typography>Date: {entry.date}</Typography>
              <Typography>Message Body: {entry.messageBody}</Typography>
              <Typography>Attachments: {entry.attachments}</Typography>
              <br />
            </FormGroup>
          </React.Fragment>
        )
      })}
      <Button
        className={styles.buttons}
        align="center"
        variant="outlined"
        fullWidth
        color="primary"
        onClick={() => handleClick()}
      >
        Create New Message
      </Button>
    </main>
  )
};

export default Chat;