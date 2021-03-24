import React from "react";

export const navLinks = [
  {
    id: 1,
    url: "/",
    text: "Home",
  },
  {
    id: 2,
    url: "/institutions",
    text: "Institutions",
  },
  {
    id: 3,
    url: "/account",
    text: "My Account",
  },
];

export const audits = [
  {
    tenantid: "1",
    type: "FB",
    tenantName: "Kopitiam",
    timeRemaining: 30,
    status: "unresolved",
    date: "-",
    institution: "CGH",
    score: "",
  },
  {
    tenantid: "2",
    type: "FB",
    tenantName: "1983",
    timeRemaining: 2,
    status: "unresolved",
    date: "-",
    institution: "KKH",
    score: "",
  },
  {
    tenantid: "3",
    type: "FB",
    tenantName: "7-Eleven",
    timeRemaining: 10,
    status: "unresolved",
    date: "-",
    institution: "SGH",
    score: "",
  },
  {
    tenantid: "4",
    type: "Non-FB",
    tenantName: "Kcuts",
    timeRemaining: 0,
    status: "resolved",
    date: "12-02-20",
    institution: "SKH",
    score: "",
  },
  {
    tenantid: "5",
    type: "FB",
    tenantName: "Kaki Makan",
    timeRemaining: 0,
    status: "resolved",
    date: "27-02-20",
    institution: "NCCS",
    score: "",
  },
  {
    tenantid: "6",
    type: "FB",
    tenantName: "7-Eleven",
    timeRemaining: 0,
    status: "resolved",
    date: "13-01-20",
    institution: "NDCS",
    score: "",
  },
  {
    tenantid: "7",
    type: "FB",
    tenantName: "myNonna's",
    timeRemaining: 2,
    status: "unresolved",
    date: "-",
    institution: "NHCS",
    score: "",
  },
  {
    tenantid: "8",
    type: "Non-FB",
    tenantName: "Comics Connection",
    timeRemaining: 10,
    status: "unresolved",
    date: "-",
    institution: "SNEC",
    score: "",
  },
];

export const tenants = [
  {
    tenantid: "1",
    tenantName: "Kopitiam",
    timeRemaining: 30,
    status: "unresolved",
    institution: "1",
    latestScore: 0,
    fbChecklist: [
      {
        id: "1",
        category: "A",
        text: "FB-A-Question-1",
        checked: false,
        comment: "",
      },
      {
        id: "2",
        category: "A",
        text: "FB-A-Question-2",
        checked: false,
        comment: "",
      },
      {
        id: "3",
        category: "A",
        text: "FB-A-Question-3",
        checked: false,
        comment: "",
      },
      {
        id: "4",
        category: "A",
        text: "FB-A-Question-4",
        checked: false,
        comment: "",
      },
      {
        id: "5",
        category: "B",
        text: "FB-B-Question-1",
        checked: false,
        comment: "",
      },
      {
        id: "6",
        category: "B",
        text: "FB-B-Question-2",
        checked: false,
        comment: "",
      },
      {
        id: "7",
        category: "B",
        text: "FB-B-Question-3",
        checked: false,
        comment: "",
      },
      {
        id: "8",
        category: "B",
        text: "FB-B-Question-4",
        checked: false,
        comment: "",
      },
    ],
  },
  {
    tenantid: "2",
    tenantName: "1983",
    timeRemaining: 2,
    status: "unresolved",
    institution: "2",
    latestScore: 0,
    score: [{ date: "12-3-2021", value: 0 }],
  },
  {
    tenantid: "3",
    tenantName: "7-Eleven",
    timeRemaining: 10,
    status: "unresolved",
    institution: "3",
    latestScore: 0,
    score: [{ date: "12-3-2021", value: 0 }],
    fbChecklist: [
      {
        id: "1",
        category: "A",
        text: "FB-A-Question-1",
        checked: false,
        comment: "",
      },
      {
        id: "2",
        category: "A",
        text: "FB-A-Question-2",
        checked: false,
        comment: "",
      },
      {
        id: "3",
        category: "A",
        text: "FB-A-Question-3",
        checked: false,
        comment: "",
      },
      {
        id: "4",
        category: "A",
        text: "FB-A-Question-4",
        checked: false,
        comment: "",
      },
      {
        id: "5",
        category: "B",
        text: "FB-B-Question-1",
        checked: false,
        comment: "",
      },
      {
        id: "6",
        category: "B",
        text: "FB-B-Question-2",
        checked: false,
        comment: "",
      },
      {
        id: "7",
        category: "B",
        text: "FB-B-Question-3",
        checked: false,
        comment: "",
      },
      {
        id: "8",
        category: "B",
        text: "FB-B-Question-4",
        checked: false,
        comment: "",
      },
    ],
  },
  {
    tenantid: "4",
    tenantName: "Kcuts",
    timeRemaining: 0,
    status: "resolved",
    institution: "4",
    latestScore: 0,
    score: [{ date: "12-3-2021", value: 0 }],
    fbChecklist: [
      {
        id: "1",
        category: "A",
        text: "FB-A-Question-1",
        checked: false,
        comment: "",
      },
      {
        id: "2",
        category: "A",
        text: "FB-A-Question-2",
        checked: false,
        comment: "",
      },
      {
        id: "3",
        category: "A",
        text: "FB-A-Question-3",
        checked: false,
        comment: "",
      },
      {
        id: "4",
        category: "A",
        text: "FB-A-Question-4",
        checked: false,
        comment: "",
      },
      {
        id: "5",
        category: "B",
        text: "FB-B-Question-1",
        checked: false,
        comment: "",
      },
      {
        id: "6",
        category: "B",
        text: "FB-B-Question-2",
        checked: false,
        comment: "",
      },
      {
        id: "7",
        category: "B",
        text: "FB-B-Question-3",
        checked: false,
        comment: "",
      },
      {
        id: "8",
        category: "B",
        text: "FB-B-Question-4",
        checked: false,
        comment: "",
      },
    ],
  },
  {
    tenantid: "5",
    tenantName: "Kaki Makan",
    timeRemaining: 0,
    status: "resolved",
    date: "27-02-20",
    institution: "5",
    latestScore: 0,
    score: [{ date: "12-3-2021", value: 0 }],
    fbChecklist: [
      {
        id: "1",
        category: "A",
        text: "FB-A-Question-1",
        checked: false,
        comment: "",
      },
      {
        id: "2",
        category: "A",
        text: "FB-A-Question-2",
        checked: false,
        comment: "",
      },
      {
        id: "3",
        category: "A",
        text: "FB-A-Question-3",
        checked: false,
        comment: "",
      },
      {
        id: "4",
        category: "A",
        text: "FB-A-Question-4",
        checked: false,
        comment: "",
      },
      {
        id: "5",
        category: "B",
        text: "FB-B-Question-1",
        checked: false,
        comment: "",
      },
      {
        id: "6",
        category: "B",
        text: "FB-B-Question-2",
        checked: false,
        comment: "",
      },
      {
        id: "7",
        category: "B",
        text: "FB-B-Question-3",
        checked: false,
        comment: "",
      },
      {
        id: "8",
        category: "B",
        text: "FB-B-Question-4",
        checked: false,
        comment: "",
      },
    ],
  },
  {
    tenantid: "6",
    tenantName: "7-Eleven",
    timeRemaining: 0,
    status: "resolved",
    date: "13-01-20",
    institution: "6",
    latestScore: 0,
    score: [{ date: "12-3-2021", value: 0 }],
    fbChecklist: [
      {
        id: "1",
        category: "A",
        text: "FB-A-Question-1",
        checked: false,
        comment: "",
      },
      {
        id: "2",
        category: "A",
        text: "FB-A-Question-2",
        checked: false,
        comment: "",
      },
      {
        id: "3",
        category: "A",
        text: "FB-A-Question-3",
        checked: false,
        comment: "",
      },
      {
        id: "4",
        category: "A",
        text: "FB-A-Question-4",
        checked: false,
        comment: "",
      },
      {
        id: "5",
        category: "B",
        text: "FB-B-Question-1",
        checked: false,
        comment: "",
      },
      {
        id: "6",
        category: "B",
        text: "FB-B-Question-2",
        checked: false,
        comment: "",
      },
      {
        id: "7",
        category: "B",
        text: "FB-B-Question-3",
        checked: false,
        comment: "",
      },
      {
        id: "8",
        category: "B",
        text: "FB-B-Question-4",
        checked: false,
        comment: "",
      },
    ],
  },
  {
    tenantid: "7",
    tenantName: "myNonna's",
    timeRemaining: 2,
    status: "unresolved",
    date: "-",
    institution: "1",
    latestScore: 0,
    score: [{ date: "12-3-2021", value: 0 }],
    fbChecklist: [
      {
        id: "1",
        category: "A",
        text: "FB-A-Question-1",
        checked: false,
        comment: "",
      },
      {
        id: "2",
        category: "A",
        text: "FB-A-Question-2",
        checked: false,
        comment: "",
      },
      {
        id: "3",
        category: "A",
        text: "FB-A-Question-3",
        checked: false,
        comment: "",
      },
      {
        id: "4",
        category: "A",
        text: "FB-A-Question-4",
        checked: false,
        comment: "",
      },
      {
        id: "5",
        category: "B",
        text: "FB-B-Question-1",
        checked: false,
        comment: "",
      },
      {
        id: "6",
        category: "B",
        text: "FB-B-Question-2",
        checked: false,
        comment: "",
      },
      {
        id: "7",
        category: "B",
        text: "FB-B-Question-3",
        checked: false,
        comment: "",
      },
      {
        id: "8",
        category: "B",
        text: "FB-B-Question-4",
        checked: false,
        comment: "",
      },
    ],
  },
  {
    tenantid: "8",
    tenantName: "Comics Connection",
    timeRemaining: 10,
    status: "unresolved",
    date: "-",
    institution: "2",
    latestScore: 0,
    score: [{ date: "12-3-2021", value: 0 }],
    fbChecklist: [
      {
        id: "1",
        category: "A",
        text: "FB-A-Question-1",
        checked: false,
        comment: "",
      },
      {
        id: "2",
        category: "A",
        text: "FB-A-Question-2",
        checked: false,
        comment: "",
      },
      {
        id: "3",
        category: "A",
        text: "FB-A-Question-3",
        checked: false,
        comment: "",
      },
      {
        id: "4",
        category: "A",
        text: "FB-A-Question-4",
        checked: false,
        comment: "",
      },
      {
        id: "5",
        category: "B",
        text: "FB-B-Question-1",
        checked: false,
        comment: "",
      },
      {
        id: "6",
        category: "B",
        text: "FB-B-Question-2",
        checked: false,
        comment: "",
      },
      {
        id: "7",
        category: "B",
        text: "FB-B-Question-3",
        checked: false,
        comment: "",
      },
      {
        id: "8",
        category: "B",
        text: "FB-B-Question-4",
        checked: false,
        comment: "",
      },
    ],
  },
];

export const categories = ["all", "unresolved", "resolved"];

export const auditors = [
  {
    id: "1",
    name: "Marcus Ho",
    dob: "19 January 1998",
  },
];

export const institutions = [
  {
    id: "1",
    name: "CGH",
    tenantNames: [],
    tenantIDs: [],
    imageUrl: "../images/cgh.png",
  },
  {
    id: "2",
    name: "KKH",
    tenantNames: [],
    tenantIDs: [],
    imageUrl: "../images/kkh.png",
  },
  {
    id: "3",
    name: "SGH",
    tenantNames: [],
    tenantIDs: [],
    imageUrl: "../images/sgh.png",
  },
  {
    id: "4",
    name: "SKH",
    tenantNames: [],
    tenantIDs: [],
    imageUrl: "../images/skh.png",
  },
  {
    id: "5",
    name: "NCCS",
    tenantNames: [],
    tenantIDs: [],
    imageUrl: "../images/nccs.png",
  },
  {
    id: "6",
    name: "NDCS",
    tenantNames: [],
    tenantIDs: [],
    imageUrl: "../images/ndcs.png",
  },
];

export const checklistTypes = [
  "FB Checklist",
  "Non-FB Checklist",
  "SM Checklist",
];

export const fbChecklist = [
  {
    id: "1",
    category: "A",
    text:
      "Shop is open and ready to service patients/visitors according to operating hours.",
    checked: false,
    modalOpen: false,
  },
  {
    id: "2",
    category: "A",
    text: "Staff Attendance: adequate staff for peak and non-peak hours.",
    checked: false,
    modalOpen: false,
  },
  {
    id: "3",
    category: "A",
    text: "At least one (1) clearly assigned person in-charge on site.",
    checked: false,
    modalOpen: false,
  },
  {
    id: "4",
    category: "A",
    text:
      "Staff who are unfit for work due to illness should not report to work).",
    checked: false,
    modalOpen: false,
  },
  {
    id: "5",
    category: "B",
    text: "FB-B-Question-1",
    checked: false,
    modalOpen: false,
  },
  {
    id: "6",
    category: "B",
    text: "FB-B-Question-2",
    checked: false,
    modalOpen: false,
  },
  {
    id: "7",
    category: "B",
    text: "FB-B-Question-3",
    checked: false,
    modalOpen: false,
  },
  {
    id: "8",
    category: "B",
    text: "FB-B-Question-4",
    checked: false,
    modalOpen: false,
  },
];
