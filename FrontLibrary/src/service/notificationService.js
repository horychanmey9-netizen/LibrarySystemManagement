
const STORAGE_KEY = "library_notifications";
const EVENT_NAME = "library-notifications-updated";

const defaultNotifications = [
  {
    id: 1,
    sender: "Library System",
    subject: "Your book is due soon",
    message:
      "A Brief History of Time is due in 2 days. Please return or renew the book before the due date.",
    type: "Borrowing",
    date: "10:24 AM",
    read: false,
    starred: true,
    selected: false,
  },
  {
    id: 2,
    sender: "Library System",
    subject: "Fine payment reminder",
    message:
      "You have an outstanding fine of $8.50. Please make a payment to avoid additional charges.",
    type: "Fine",
    date: "9:10 AM",
    read: false,
    starred: false,
    selected: false,
  },
  {
    id: 3,
    sender: "Library System",
    subject: "Book successfully borrowed",
    message:
      "You have successfully borrowed Clean Code. Your due date is August 25, 2026.",
    type: "Borrowing",
    date: "Yesterday",
    read: false,
    starred: false,
    selected: false,
  },
  {
    id: 4,
    sender: "Library System",
    subject: "Book returned successfully",
    message:
      "Sapiens has been successfully returned. Thank you for returning the book on time.",
    type: "Return",
    date: "Yesterday",
    read: true,
    starred: false,
    selected: false,
  },
  {
    id: 5,
    sender: "Library System",
    subject: "New books are available",
    message:
      "A new collection of technology books has been added to the library.",
    type: "Library",
    date: "Aug 10",
    read: true,
    starred: true,
    selected: false,
  },
  {
    id: 6,
    sender: "Library System",
    subject: "Fine payment received",
    message:
      "Your payment of $3.00 for Clean Code has been successfully received.",
    type: "Fine",
    date: "Aug 09",
    read: true,
    starred: false,
    selected: false,
  },
  {
    id: 7,
    sender: "Library System",
    subject: "Book overdue",
    message:
      "The book Atomic Habits is overdue. Please return the book as soon as possible.",
    type: "Overdue",
    date: "Aug 08",
    read: false,
    starred: false,
    selected: false,
  },
  {
    id: 8,
    sender: "Library System",
    subject: "Library maintenance notice",
    message:
      "The library system will be under maintenance this weekend from 10:00 PM to 12:00 AM.",
    type: "System",
    date: "Aug 07",
    read: true,
    starred: false,
    selected: false,
  },
  {
    id: 9,
    sender: "Library System",
    subject: "Welcome to the library",
    message:
      "Welcome to our library system. You can now browse, borrow, and manage your books.",
    type: "System",
    date: "Aug 01",
    read: true,
    starred: false,
    selected: false,
  },
];


// ===============================
// GET ALL NOTIFICATIONS
// ===============================
export function getNotifications() {
  const saved = localStorage.getItem(STORAGE_KEY);

  if (saved) {
    try {
      return JSON.parse(saved);
    } catch (error) {
      console.error("Invalid notification data:", error);
    }
  }

  const data = JSON.parse(
    JSON.stringify(defaultNotifications)
  );

  saveNotifications(data);

  return data;
}


// ===============================
// SAVE NOTIFICATIONS
// ===============================
export function saveNotifications(notifications) {
  localStorage.setItem(
    STORAGE_KEY,
    JSON.stringify(notifications)
  );

  // Tell all components that data changed
  window.dispatchEvent(
    new CustomEvent(EVENT_NAME)
  );
}


// ===============================
// GET UNREAD COUNT
// ===============================
export function getUnreadCount() {
  return getNotifications().filter(
    notification => !notification.read
  ).length;
}


// ===============================
// MARK ONE AS READ
// ===============================
export function markAsRead(id) {
  const notifications = getNotifications();

  const notification = notifications.find(
    item => item.id === Number(id)
  );

  if (notification) {
    notification.read = true;
    saveNotifications(notifications);
  }

  return notifications;
}


// ===============================
// MARK ALL AS READ
// ===============================
export function markAllAsRead() {
  const notifications = getNotifications();

  notifications.forEach(notification => {
    notification.read = true;
  });

  saveNotifications(notifications);

  return notifications;
}


// ===============================
// DELETE NOTIFICATION
// ===============================
export function deleteNotification(id) {
  const notifications = getNotifications().filter(
    notification => notification.id !== Number(id)
  );

  saveNotifications(notifications);

  return notifications;
}


// ===============================
// TOGGLE STAR
// ===============================
export function toggleStar(id) {
  const notifications = getNotifications();

  const notification = notifications.find(
    item => item.id === Number(id)
  );

  if (notification) {
    notification.starred = !notification.starred;
    saveNotifications(notifications);
  }

  return notifications;
}


// ===============================
// ADD NOTIFICATION
// ===============================
export function addNotification(notification) {
  const notifications = getNotifications();

  const newNotification = {
    id: Date.now(),
    sender: "Library System",
    read: false,
    starred: false,
    selected: false,
    ...notification,
  };

  notifications.unshift(newNotification);

  saveNotifications(notifications);

  return newNotification;
}


// ===============================
// EVENT NAME
// ===============================
export function notificationUpdatedEvent() {
  return EVENT_NAME;
}

