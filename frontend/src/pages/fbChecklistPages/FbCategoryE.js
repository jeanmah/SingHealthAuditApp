import React, {useState} from "react";
import {fbChecklist} from "../../data";
import {NavLink} from "react-router-dom";

function FbCategoryE() {
  const fbChecklistCategory = "E";

  const filteredChecklistLines = fbChecklist.filter(
    (checklistLine) => checklistLine.category === fbChecklistCategory
  );

  return (
    <div className="category-head">
      <h2>FB Checklist Category E</h2>
      {filteredChecklistLines.map((checklistLine, index) => {
        const {id, category, text} = checklistLine;
        return (
          <article>
            <div>{text}</div>
          </article>
        )
      })}
      <NavLink exact to="/fbCategoryD"><button>Previous</button></NavLink>
    </div>
  )
}

export default FbCategoryE;