import { FormGroup } from "@material-ui/core";
import {
  Typography,
  Button,
  Grid,
  TextField,
  List,
  ListItem,
} from "@material-ui/core";
import React, { useState, useEffect, useContext, useLocation } from "react";
import { useParams } from "react-router-dom";

import { Context } from "../Context";
import Navbar from "../Navbar";
import useStyles from "../styles";
import { getDateString, getTimeString } from "../components/utils";

function Chat() {
  const { chatId, storeName, accId } = useParams(); // typeof chatId: String
  const [chatEntriesState, setChatEntriesState] = useState([]);
  const [subjectState, setSubjectState] = useState("");
  const [bodyState, setBodyState] = useState("");
  // Use a state to force
  const {
    allChatsOfUserState,
    getChatEntriesOfUser,
    postChatEntry,
    accountState,
    chatSubmitState,
  } = useContext(Context);
  const styles = useStyles();
  const { acc_id } = accountState;

  const [allChatsState, setAllChatsState] = useState([]);

  const chatsArray = [];
  const chatEntriesArray = [];
  const chatsEntriesDict = {};

  const parentChatId = "2";
  const numLastestChatEntries = "1";

  useEffect(() => {
    async function getResponse() {
      try {
        await getChatEntriesOfUser(chatId).then((response) => {
          console.log("Chat: allChatsOfUser: " + response.data);
          setChatEntriesState(response.data.reverse());
          //console.log("state: " + chatEntriesState);
          allChatsOfUserState.map((chat, index) => {
            //console.log(chat.chat_id);
            //console.log(typeof chat.chat_id);
            // if (chat.chat_id === parseInt(chatId)) {
            //   currentChat = chat;
            //   console.log("yes!")
            // }
          });
          //console.log(currentChat.chat_id);
          //console.log(chatId);
          //console.log(typeof chatId);
        });
      } catch {
        console.log("Failed to retrive allChatsOfUser");
      }
    }
    getResponse();
  }, [chatSubmitState]);

  //console.log(currentChat.chat_id);

  // const ChatHeader = () => {
  //   let friend_id = 0;
  //   if (auditor_id === acc_id) {
  //     friend_id = tenant_id;
  //   } else if (tenant_id === acc_id) {
  //     friend_id = auditor_id;
  //   } else {
  //     friend_id = null;
  //   }
  //   return (
  //     <div>Chat with {friend_id}</div>
  //   )
  // }

  // Check if the message is sent by the user, or received by the user
  function isMine(sender_id) {
    return sender_id === acc_id;
  }

  // Handle the SEND MESSAGE button click
  function handleClick() {
    let parentChatId = chatId;
    let subject = subjectState;
    let messageBody = bodyState;
    let attachments = null; // What is JSON node?
    let date = getDateString(new Date());
    let time = getTimeString();
    console.log("This is calling postNewChatEntry");
    console.log("Date: " + date);
    console.log("Time: " + time);
    console.log("Chat ID: " + parentChatId);
    console.log("Subject: " + subject);
    console.log("Message Body: " + messageBody);
    postChatEntry(parentChatId, subject, messageBody, attachments);
  }

  function subjectChangeHandler(subject_input) {
    setSubjectState(subject_input);
  }

  function bodyChangeHandler(body_input) {
    setBodyState(body_input);
  }

  return (
    <main className={styles.main}>
      <Navbar />
      <br />
      <Typography variant="h5" align="center">
        {storeName}
      </Typography>
      <Typography variant="body2" align="center">
        ID: {accId}
      </Typography>
      <br />
      <ul className={styles.chat_entries_list}>
        {chatEntriesState.map((entry, index) => {
          return (
            <React.Fragment key={index}>
              <li
                item
                className={
                  isMine(entry.sender_id)
                    ? styles.rightBubble
                    : styles.leftBubble
                }
              >
                <Grid item xs={12} sm container>
                  <Grid item xs container direction="column" spacing={2}>
                    <Grid item xs>
                      <Typography variant="subtitle2" color="textSecondary">
                        Subject: {entry.subject}
                      </Typography>
                      <Typography variant="body1">
                        {entry.messageBody}
                      </Typography>
                    </Grid>
                  </Grid>
                  <Grid item>
                    <Typography variant="body2" color="textSecondary">
                      {entry.date}, {entry.time}
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                      {entry.attachments}
                    </Typography>
                  </Grid>
                </Grid>
              </li>
            </React.Fragment>
          );
        })}
      </ul>
      <div className={styles.chat_entry_edit}>
        <FormGroup column="true">
          <TextField
            className={styles.message_input}
            label="Subject"
            variant="outlined"
            onChange={(e) => subjectChangeHandler(e.target.value)}
          />
          <TextField
            className={styles.message_input}
            label="Message"
            variant="outlined"
            onChange={(e) => bodyChangeHandler(e.target.value)}
          />
        </FormGroup>

        <Button
          className={styles.big_buttons}
          align="center"
          variant="outlined"
          fullWidth
          color="primary"
          onClick={() => handleClick()}
        >
          Send Message
        </Button>
      </div>
    </main>
  );
}

export default Chat;
