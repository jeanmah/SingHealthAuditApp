import React, { useContext, useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import { Context } from '../Context';
import { makeStyles } from '@material-ui/core/styles';
import Accordion from '@material-ui/core/Accordion';
import AccordionSummary from '@material-ui/core/AccordionSummary';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ChatEntriesCards from "./ChatEntriesCard";
import { Button } from "@material-ui/core";
import { FormGroup } from "@material-ui/core";


const ChatCards = (props) => {
  return (
    <div>
      <br />
      <NavLink to={`/chat/${props.chat_id}`}>
        <Button>
          Chat ID: {props.chat_id}
          Tenant ID: {props.tenant_id}
        </Button>
      </NavLink>
    </div>
  )
}

export default ChatCards;