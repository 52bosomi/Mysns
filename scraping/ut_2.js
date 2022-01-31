import { io } from "socket.io-client";

const socket = io("https://example.com", {
  path: "/my-custom-path/"
});