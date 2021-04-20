import React, { useEffect, useContext, useState } from "react";
import { Typography, TextField, Button, FormGroup, Grid } from "@material-ui/core";

import { Context } from "../Context";
import Navbar from "../Navbar";
import useStyles from "../styles";
import ChatCards from "../components/ChatCards";

function Chat() {

  const {
    setAllChatsOfUserState,
    allChatsOfUserState,
    getAllChatsOfUser,
    postCreateNewChat,
    accountState,
    chatSubmitState,
  } = useContext(Context);

  const [allChatsState, setAllChatsState] = useState([]);
  const [auditorIdState, setAuditorIdState] = useState("");
  const [tenantIdState, setTenantIdState] = useState("");
  const { role_id, acc_id } = accountState;
  const styles = useStyles();
  const chatsArray = [];

  function targetUserChangeHandler(target_id) {
    if (target_id === null) {
      return; // If no target_id is entered: show alert
    }
    if (role_id === "Auditor") {
      setAuditorIdState(acc_id);
      setTenantIdState(target_id);
    } else if (role_id == "Tenant") {
      setTenantIdState(acc_id);
      setAuditorIdState(target_id);
    } else {
      console.log("Invalid auditor/tenant ID");
    }
  }

  useEffect(() => {
    //getAllChatsOfUser();
    async function getResponse() {
      try{
        await getAllChatsOfUser().then((response) => {
          console.log("allChatsOfUser: " + response.data);
          setAllChatsOfUserState(response.data);
        });
      } catch {
        console.log("Failed to retrive allChatsOfUser");
      }
    }
    getResponse();
  }, [chatSubmitState]);

  function handleClick() {
    console.log("Chat calling postNewChat");
    console.log("auditor id: " + auditorIdState);
    console.log("tenant id: " + tenantIdState);
    postCreateNewChat(auditorIdState, tenantIdState);
  }

  return (
    <main className={styles.main}>
      <Navbar />
      <div className={styles.chat_edit}>
        <FormGroup column="true">
          <TextField 
            className={styles.message_input} 
            label="Receiver ID" 
            variant="outlined" 
            align="center"
            onChange={(e) => targetUserChangeHandler(e.target.value)}
          />
        </FormGroup>
        
        <Button 
          align="center"
          variant="outlined"
          color="secondary"
          className={styles.big_buttons}
          onClick={() => handleClick()}
        >
          Create Chat
        </Button>
      </div>
      <br />
      <div className={styles.chat_list}>
        {allChatsOfUserState.map((chat, index) => {
          return (
            <React.Fragment key={index}>
              <ChatCards chat={chat}/>
            </React.Fragment>
          );
        })}
      </div>
      
      <br />
      <br />
    </main>
  )
}

export default Chat;