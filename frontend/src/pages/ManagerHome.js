import React, { useState, useContext, useEffect } from "react";
import { Link } from "react-router-dom";
import { Button, IconButton, TextField, FormControl, InputLabel, Select, Typography, Grid } from "@material-ui/core";
import { InputAdornment, DialogActions, DialogContent, DialogTitle, Dialog, DialogContentText } from "@material-ui/core";
import SearchIcon from '@material-ui/icons/Search';

import Navbar from "../Navbar";
import useStyles from "../styles";
import { Context } from "../Context"
import NotificationRow from "../components/NotificationRow";


function ManagerHome() {

  const styles = useStyles();
  const { 
    accountState, 
    allAvailableNotificationsState,
    getAllAvailableNotifications,
    currentNotificationsState,
    getCurrentNotifications,
    notificationsByNotificationIdState,
    getNotificationByNotificationId,
    chatSubmitState,
    postNewNotification,
    postModifyNotification,
    deleteNotification,
  } = useContext(Context);

  const [displayedNotificationsState, setDisplayedNotificationsState] = useState([]);
  const [notificationRangeState, setNotificationRangeState] = useState("current");
  const [searchBarInputState, setSearchBarInputState] = useState("");

  // States for inputs
  const [titleState, setTitleState] = useState("");
  const [messageState, setMessageState] = useState("");
  const [receiptDateDisplayed, setReceiptDateDisplayed] = useState(new Date());
  const [receiptDateState, setReceiptDateState] = useState(""); // Default today
  const [endDateDisplayed, setEndDateDisplayed] = useState(new Date());
  const [endDateState, setEndDateState] = useState(""); // Default one month later
  const [receiversState, setReceiversState] = useState(7); // Default all users

  // States for dialogs
  const [postNewDialogState, setPostNewDialogState] = useState(false);
  const [modifyDialogState, setModifyDialogState] = useState(false);
  const [deleteDialogState, setDeleteDialogState] = useState(false);
  const [tipDialogState, setTipDialogState] = useState(false);
  const [successDialogState, setSuccessDialogState] = useState(false);

  const { role_id } = accountState;

  function handleTitleChange(input_title) {setTitleState(input_title);}
  function handleMessageChange(input_message) {setMessageState(input_message);}
  function handleReceiptDateChange(input_date) {
    setReceiptDateState(input_date); // String passed to backend
    setReceiptDateDisplayed(input_date); // Date object to display
  }
  function handleEndDateChange(input_date) {
    setEndDateState(input_date); // String passed to backend
    setEndDateDisplayed(input_date); // Date object to display
  }
  function handleReceiverChange(input_receiver) {setReceiversState(parseInt(input_receiver));}

  function openPostNewDialog() {setPostNewDialogState(true);}
  function closePostNewDialog() {setPostNewDialogState(false);}
  function openModifyDialog() {setModifyDialogState(true);}
  function closeModifyDialog() {setModifyDialogState(false);}
  function openDeleteDialog() {setDeleteDialogState(true);}
  function closeDeleteDialog() {setDeleteDialogState(false);}
  function openTipDialog() {setTipDialogState(true);}
  function closeTipDialog() {setTipDialogState(false);}
  function openSuccessDialog() {setSuccessDialogState(true);}
  function closeSuccessDialog() {setSuccessDialogState(false);}

  function submitNewAccouncement() {
    console.log("Submitting new announcement...");
    postNewNotification(titleState, messageState, receiptDateState, endDateState, receiversState);
    openSuccessDialog();
  }

  function modifyPastAnnouncement() {
    console.log("Modifying announcement...");
    postModifyNotification(1, "Modified Test1", "Hello there!!!", "20/04/2021", "30/04/2021", 7);
    openSuccessDialog();
  }

  function deleteAnnouncement() {
    console.log("Deleting announcement...");
    deleteNotification(4);
    openSuccessDialog();
  }

  function handleSearchButtonClick() {
    console.log("Submitting search bar input: " + searchBarInputState);
    console.log(typeof searchBarInputState);
    // if (searchBarInputState.length === 4) {
    //   setNotificationRangeState("by_creator_id");
    // } else {
    //   setNotificationRangeState("by_notification_id");
    // }
    // setNotificationRangeState("all_available");
  }

  function handleSearchBarChange(search_input) {
    setSearchBarInputState(search_input);
  }

  function handleClick() {
    //setDisplayedNotificationsState(allAvailableNotificationsState);
    console.log("Search Button Clicked!");
    console.log(searchBarInputState);
    console.log("REAL notificationsState: " + allAvailableNotificationsState);
  }
  function handleNewAnnouncementClick() {
    console.log("Posting new announcement...");
    openPostNewDialog();
  }
  function handleModifyAnnouncementClick() {
    console.log("Modifying existing announcement...");
    openModifyDialog();
  }
  function handleDeleteAnnouncementClick() {
    console.log("Deleting existing announcement...");
    openDeleteDialog();
  }
  function handleTipClick() {
    console.log("Openning accouncement tips...");
    openTipDialog();
  }
  function handleSuccessClick() {
    console.log("Success dialog click...");
    closeSuccessDialog();
    closePostNewDialog();
    closeModifyDialog();
    closeDeleteDialog();
  }

  useEffect(() => {
    async function getResponse(role_id) {
        try{
          await getAllAvailableNotifications().then((response) => {
            console.log("All available notifications: " + response.data);
            setDisplayedNotificationsState(response.data);
            console.log("Notifications state: " + displayedNotificationsState);
          })
        } catch {
          console.log("Failed to retrive allAvailableNotifications");
        }

        // try{
        //   await getCurrentNotifications().then((response) => {
        //     console.log("Current notifications: " + response.data);
        //     setNotificationsState(response.data);
        //     console.log("Notifications state: " + notificationsState);
        //   })
        // } catch {
        //   console.log("Failed to retrive currentNotifications");
        // }
    };
    getResponse();
    //getCurrentNotifications();
    //getNotificationByNotificationId();
  }, [chatSubmitState]);

  return (
    <main className={styles.main}>
      <Navbar />
<<<<<<< HEAD
      <div>This is manager homepage</div>
=======
      <div className={styles.body}>
        <TextField
          className={styles.search_bar} 
          label="Search Notification ID/Creator ID" 
          variant="outlined" 
          InputProps={{endAdornment: (<InputAdornment><IconButton onClick={handleClick}><SearchIcon/></IconButton></InputAdornment>)}}
          onChange={(e) => handleSearchBarChange(e.target.value)}
        />
        <div className={styles.annoucement_title_div}>
          <Typography variant="h6" className={styles.annoucement_title}>Announcements</Typography>
        </div>
        <div className={styles.announcement_list}>
          {displayedNotificationsState.map((notification, index) => {
            return (
              <NotificationRow notification={notification} key={index}/>
            )
          })}
        </div>
        <div className={styles.post_new_accouncement_div}>
          <Button className={styles.big_bottom_buttons} align="center" variant="outlined" color="primary" onClick={handleNewAnnouncementClick}>Post New Announcement</Button>
        </div>

        {/* Dialog used for posting new announcement */}
        <Dialog
          className={styles.post_new_announcement_dialog}
          open={postNewDialogState}
          onClose={closePostNewDialog}
          aria-labelledby="alert-dialog-title"
          aria-describedby="alert-dialog-description"
        >
          <DialogTitle id="alert-dialog-title">{"New Announcement"}</DialogTitle>
          <DialogContent>
            <TextField className={styles.new_announcement_input} value={titleState} label="Title" variant="outlined" onChange={(e) => handleTitleChange(e.target.value)}/>
            <TextField className={styles.new_announcement_input} value={messageState} label="Message" variant="outlined" onChange={(e) => handleMessageChange(e.target.value)}/>
            <TextField className={styles.new_announcement_input} value={receiptDateState} label="Receipt Date (DD/MM/YYYY)" variant="outlined" onChange={(e) => handleReceiptDateChange(e.target.value)}/>
            <TextField className={styles.new_announcement_input} value={endDateState} label="End Date (DD/MM/YYYY)" variant="outlined" onChange={(e) => handleEndDateChange(e.target.value)}/>
            {/* <MuiPickersUtilsProvider utils={DateFnsUtils}>
              <Grid container justify="space-around">
                <KeyboardDatePicker
                  className={styles.dialog_date_picker}
                  margin="normal"
                  label="Receipt Date"
                  format="yyyy-MM-dd"
                  value={receiptDateDisplayed}
                  onChange={handleReceiptDateChange}
                  KeyboardButtonProps={{
                    'aria-label': 'change date',
                  }}
                />
                <KeyboardDatePicker
                  className={styles.dialog_date_picker}
                  label="End Date"
                  format="yyyy-MM-dd"
                  value={endDateDisplayed}
                  onChange={handleEndDateChange}
                  KeyboardButtonProps={{
                    'aria-label': 'change date',
                  }}
                />
              </Grid>
            </MuiPickersUtilsProvider> */}
            <FormControl variant="outlined" className={styles.dialog_selector}>
              <InputLabel>Receivers</InputLabel>
              <Select native label="Receivers" value={receiversState} onChange={(e) => handleReceiverChange(e.target.value)}>
                <option value={7}>All users</option>
                <option value={5}>Manager and Tenant</option>
                <option value={6}>Auditor and Tenant</option>
                <option value={2}>Only Auditor</option>
                <option value={4}>Only Tenant</option>
              </Select>
            </FormControl>
          </DialogContent>
          <DialogContent>
            <Link className={styles.dialog_link} onClick={handleTipClick}>Tips</Link>
          </DialogContent>
          <DialogActions>
            <Button onClick={closePostNewDialog} color="secondary">Cancel</Button>
            <Button onClick={submitNewAccouncement} color="primary">Continue</Button>
          </DialogActions>
        </Dialog>

        {/* Dialog used for announcement tips */}
        <Dialog
          className={styles.post_new_announcement_dialog}
          open={tipDialogState}
          onClose={closeTipDialog}
          aria-labelledby="alert-dialog-title"
          aria-describedby="alert-dialog-description"
        >
          <DialogTitle id="alert-dialog-title">{"Tips"}</DialogTitle>
          <DialogContent>
            <DialogContentText id="alert-dialog-description">How to create new announcements?</DialogContentText>
            <DialogContentText id="alert-dialog-description">* "Title" and "Message" is where you should post your announcement information</DialogContentText>
            <DialogContentText id="alert-dialog-description">* "Receipt Date" is the day when the receipients should start to see the announcement</DialogContentText>
            <DialogContentText id="alert-dialog-description">* "End Date" is the day when the recipients should stop getting the notification</DialogContentText>
            <DialogContentText id="alert-dialog-description">* "Receivers" indicates the range of receipients who can see the announcement.</DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={closeTipDialog} color="primary">Ok</Button>
          </DialogActions>
        </Dialog>

        {/* Dialog used for handling dialog states upon successful submission */}
        <Dialog
          className={styles.post_new_announcement_dialog}
          open={successDialogState}
          onClose={closeSuccessDialog}
          aria-labelledby="alert-dialog-title"
          aria-describedby="alert-dialog-description"
        >
          <DialogTitle id="alert-dialog-title">{"Changes updated!"}</DialogTitle>
          <DialogContent>
            <DialogContentText id="alert-dialog-description">Your changes have been updated</DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleSuccessClick} color="primary">Ok</Button>
          </DialogActions>
        </Dialog>
        
      </div>
      
>>>>>>> a8b9d981ebbb5f4adcb4ece47154562ce623ac09
    </main>
  );
}

export default ManagerHome;
