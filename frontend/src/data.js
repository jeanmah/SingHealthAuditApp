import React from "react";

const users = [
  {
    username: "hannz",
    password: "test123",
    type: "auditor",
  },
  {
    username: "ZHZHZH",
    pasword: "test123",
    type: "tenant",
  },
];

export const auditorAccountDatas = [
  "acc_id", // 1003
  "appealed_audits", // null
  "branch_id", // "*"
  "completed_audits", // null
  "email", // "jeanmah828@gmail.com"
  "employee_id", // 1004332
  "first_name", // "Hannah"
  "hp", // "90270062"
  "last_name", // "Mah"
  "mgr_id", // 1001
  "outstanding_audit_ids", // null
  "role_id", // "Auditor"
  "username", // "hannz"
];

export const tenantAccountDatas = [
  "acc_id", //1004
  "audit_score", //2
  "branch_id", //"CGH"
  "email", //"yzh98640860@gmail.com"
  "employee_id", //1004516
  "first_name", //"Zhonghao"
  "hp", //"98640860"
  "last_name", //"Yang"
  "last_audit", //-1
  "past_audits", //null
  "role_id", //"Tenant"
  "store_addr", //"#01-20"
  "type_id", //"FB"
  "username", //"ZHZHZH"
];

export const managerAccountDatas = [
  "acc_id", //1001
  "branch_id", //"HQ"
  "email", //"marcushojww@gmail.com"
  "employee_id", //1002222
  "first_name", //"Marcus"
  "hp", //"+65 432432"
  "last_name", //"Ho"
  "role_id", //"Manager"
  "username", //"mcMarcus"
];

export const auditorNavLinks = [
  {
    id: 1,
    url: "/home/a",
    text: "Home",
  },
  {
    id: 2,
    url: "/announcements",
    text: "Announcement",
  },
  {
    id: 3,
    url: "/allChats",
    text: "Chat",
  },
  {
    id: 4,
    url: "/institutions",
    text: "Institutions",
  },
  {
    id: 5,
    url: "/account",
    text: "Account",
  },
];

export const tenantNavLinks = [
  {
    id: 1,
    url: "/home/t",
    text: "Home",
  },
  {
    id: 2,
    url: "/announcements",
    text: "Announcement",
  },
  {
    id: 3,
    url: "/allChats",
    text: "Chat",
  },
  {
    id: 4,
    url: "/store",
    text: "Store",
  },
  {
    id: 5,
    url: "/account",
    text: "Account",
  },
];

export const managerNavLinks = [
  {
    id: 1,
    url: "/home/m",
    text: "Home",
  },
  {
    id: 2,
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
    imageUrl: "../images/ndcs.jpeg",
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
    modalOpen: false,
  },
  {
    id: "2",
    category: "A",
    text: "Staff Attendance: adequate staff for peak and non-peak hours.",
    modalOpen: false,
  },
  {
    id: "3",
    category: "A",
    text: "At least one (1) clearly assigned person in-charge on site.",
    modalOpen: false,
  },
  {
    id: "4",
    category: "A",
    text:
      "Staff who are unfit for work due to illness should not report to work).",
    modalOpen: false,
  },
  {
    id: "5",
    category: "B",
    text:
      "Cleaning and maintenance records for equipment, ventilation and exhaust system.",
    modalOpen: false,
  },
  {
    id: "6",
    category: "B",
    text: "Adequate and regular pest control.",
    modalOpen: false,
  },
  {
    id: "7",
    category: "B",
    text: "Goods and equipment are within shop boundary.",
    modalOpen: false,
  },
  {
    id: "8",
    category: "B",
    text: "Store display/ Shop front is neat and tidy.",
    modalOpen: false,
  },
];
export const tenantImages = [
  {
    name: "7-Eleven",
    imageUrl: "../images/7-11.png",
  },
  {
    name: "168 Florist",
    imageUrl: "../images/168florist.jpeg",
  },
  {
    name: "1983",
    imageUrl: "../images/1983.jpeg",
  },
  {
    name: "Coffee Bean",
    imageUrl: "../images/coffee_bean.jpg",
  },
  {
    name: "Kopitiam",
    imageUrl: "../images/kopitiam.jpeg",
  },
  {
    name: "Delifrance",
    imageUrl: "../images/delifrance.jpeg",
  },
  {
    name: "Mr Bean",
    imageUrl: "../images/mr_bean.jpeg",
  },
];
