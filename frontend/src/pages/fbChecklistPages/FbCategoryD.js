import React, {useState} from "react";
import {fbChecklist} from "../../data";
import {NavLink} from "react-router-dom";

function FbCategoryD() {
  const fbChecklistCategory = "D";

  const filteredChecklistLines = fbChecklist.filter(
    (checklistLine) => checklistLine.category === fbChecklistCategory
  );

  return (
    <div className="category-head">
      <h2>FB Checklist Category D</h2>
      {filteredChecklistLines.map((checklistLine, index) => {
        const {id, category, text} = checklistLine;
        return (
          <article>
            <div>{text}</div>
          </article>
        )
      })}
      <NavLink exact to="/fbCategoryC"><button>Previous</button></NavLink>
      <NavLink exact to="/fbCategoryE"><button>Next</button></NavLink>
    </div>
  )
}

export default FbCategoryD;