import React, { useState, useContext } from "react";
// import ReactDOM from "react-dom";
import { Context } from "../Context";
import { FaRegTimesCircle } from "react-icons/fa";

function Modal({ questionId, tenantId }) {
  //Context: state of modal (whether it is open or not)
  const { fbChecklistState, closeQuestionModal } = useContext(Context);
  //state for comments and updating comments
  const { comment, setComment, updateTenantComment } = useContext(Context);
  //state of displayedComments
  const { tenantsState } = useContext(Context);

  //handle change in textbox by updating comment state
  const handleChange = (event) => {
    console.log(questionId);
    setComment(event.target.value);
  };
  //handle submit
  const handleSubmit = (event) => {
    updateTenantComment(tenantId, questionId);
  };

  const checklistQuestion = fbChecklistState.find((question) => {
    return question.id === questionId;
  });

  console.log(checklistQuestion.modalOpen);

  return (
    <>
      <section
        className={`${
          fbChecklistState.find((question) => {
            return question.id === questionId;
          }).modalOpen
            ? "modal-overlay show-modal"
            : "modal-overlay"
        }`}
      >
        <div className="modal-container">
          <FaRegTimesCircle
            className="modal-closebtn"
            onClick={() => {
              closeQuestionModal(questionId);
            }}
          />
          <div>Write a Comment</div>
          <textarea
            className="modal-commentbox"
            value={comment}
            onChange={(event) => {
              handleChange(event);
            }}
          />
          <button
            onClick={() => {
              handleSubmit();
            }}
          >
            Submit
          </button>
        </div>
      </section>
    </>
  );

  // document.getElementById("portal")
}

export default Modal;
