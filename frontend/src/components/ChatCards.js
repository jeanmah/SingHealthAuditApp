import React, { useContext, useEffect, useState } from "react";
<<<<<<< HEAD
import { Context } from "../Context";
import { makeStyles } from "@material-ui/core/styles";
import Accordion from "@material-ui/core/Accordion";
import AccordionSummary from "@material-ui/core/AccordionSummary";
import AccordionDetails from "@material-ui/core/AccordionDetails";
import Typography from "@material-ui/core/Typography";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import Chat from "../pages/Chat";
import ChatEntriesCards from "./ChatEntriesCard";

const useStyles = makeStyles((theme) => ({
  root: {
    width: "100%",
  },
  heading: {
    fontSize: theme.typography.pxToRem(15),
    fontWeight: theme.typography.fontWeightRegular,
  },
}));

const ChatCards = () => {
  const { allChatsOfUserState, setAllChatsOfUserState } = useContext(Context);
  //console.log(allChatsOfUserState);
  const { chatEntriesOfUserState, setChatEntriesOfUserState } = useContext(
    Context
  );
  //console.log(chatEntriesOfUserState);
  const styles = useStyles();

  return (
    <div>
      <br />
      {allChatsOfUserState.map((chat, index) => {
        return (
          <React.Fragment key={index}>
            <Accordion>
              <AccordionSummary
                expandIcon={<ExpandMoreIcon />}
                aria-controls="panel1a-content"
                id="panel1a-header"
              >
                <Typography className={styles.heading}>
                  Chat ID: {chat.chat_id} -
                </Typography>
                <Typography className={styles.heading}>
                  Tenant ID: {chat.tenant_id}
                </Typography>
              </AccordionSummary>

              <ChatEntriesCards />
            </Accordion>
            <br />
          </React.Fragment>
        );
      })}
    </div>
  );
};

export default ChatCards;
=======
import { NavLink } from "react-router-dom";
import { makeStyles } from '@material-ui/core/styles';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { Button, Grid, FormGroup, Typography } from "@material-ui/core";

import useStyles from "../styles";
import { Context } from "../Context";


const ChatCards = (props) => {

  const styles = useStyles();
  const test = 147;
  const [targetUserState, setTargetUserState] = useState({});
  const {
    getUserInfo,
    accountState,
    chatSubmitState,
  } = useContext(Context);

  // Handle chat target role type (Auditor/Tenant) and target user_id
  var target_role = "Tenant";
  var target_id;
  if (accountState.role_id === "Auditor") {
    target_role = "Tenant";
    target_id = props.chat.tenant_id.toString();
  } else if (accountState.role_id === "Tenant") {
    target_role = "Auditor";
    target_id = props.chat.auditor_id.toString();
  }

  useEffect(() => {
    getUserInfo(target_id)
      .then((response) => {
        setTargetUserState(response.data);
      })
      .catch(() => {
        console.log("Failed to retrieve tenant info");
      });
  }, []);

  return (
    <NavLink to={{pathname: `/chat/${props.chat.chat_id}`, test: test}}>
      <Button className={styles.chat_bubble}>
        <Typography variant="subtitle1">{targetUserState.username}</Typography>
        <Typography variant="body2" color="textSecondary">{target_role} ID: {target_id}</Typography>
        <Typography variant="body2" color="textSecondary">Chat ID: {props.chat.chat_id}</Typography>        
      </Button>
    </NavLink>
  )
}

export default ChatCards;
>>>>>>> a8b9d981ebbb5f4adcb4ece47154562ce623ac09
