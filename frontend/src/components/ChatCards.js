import React, { useContext, useEffect, useState } from "react";
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
