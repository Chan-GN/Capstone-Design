import React, { useState } from "react";
import Header from "../layout/Header";
import {
  SelectChangeEvent,
  Select,
  Container,
  TextField,
  Button,
  Grid,
  FormControl,
  MenuItem,
} from "@mui/material";
import axios from "axios";
import Point from "../layout/Point";
import Language from "../layout/Language";
import ReactQuill from "react-quill";
import EditorToolbar, { modules, formats } from "../layout/EditorToolbar";
import { BoardType } from "../../model/board";
/*
 * 기본 게시글 작성 UI폼
 */
const BoardWrite = () => {
  const [boardType, setBoardType] = React.useState("free");
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const handleChange = (event: SelectChangeEvent<unknown>) => {
    setBoardType(event.target.value as string);
  };

  const handleInputClick = async () => {
    const request_data = {
      title: title,
      content: content
    };

    if (boardType === "free") { // 자유 게시판인 경우
      try {
        let response = await axios({
          method: "post",
          url: "/api/freeBoards?uid=100", // 테스트를 위해 id 고정
          headers: {"Content-Type": "application/json"},
          data: JSON.stringify(request_data)
        });
        console.log("writeBoard/response: ", response);
        console.log("writeBoard/response.status: ", response.status);
        window.location.href = "/";
      } catch (err) {
        console.log("CreateBoard/handleInput/err: ", err);
      }
    }  else if (boardType === "question") { // 자유 게시판인 경우
      try {
        let response = await axios({
          method: "post",
          url: "/api/qnaBoards/100", // 테스트를 위해 id 고정
          headers: {"Content-Type": "application/json"},
          data: JSON.stringify(request_data)
        });
        console.log("writeBoard/response: ", response);
        console.log("writeBoard/response.status: ", response.status);
        window.location.href = "/";
      } catch (err) {
        console.log("CreateBoard/handleInput/err: ", err);
      }
    }
  };

  const SelectLanguage = (boardType==="question") ? (
    <Language/>
  ) : (null);

  const SelectPoint = (boardType==="question") ? (
    <Point/>
  ) : (null);
  
  const Editor = (boardType==="question") ? (
    <Grid item>
            <EditorToolbar/>
            <ReactQuill value={content} modules={modules} formats={formats} onChange={content => setContent(content)} />
            value: {content}
    </Grid>
  ) : (null);

  return (
    <>
      <Container>
        <Header />
        <Grid container direction="column" spacing={2}>
          <Grid item>
            <FormControl style={{ minWidth: "120px" }}>
              <Select value={boardType} onChange={handleChange} size="small">
                <MenuItem value={"free"} defaultChecked>
                  자유게시판
                </MenuItem>
                <MenuItem value={"question"}>Q&A게시판</MenuItem>
                <MenuItem value={"recruit"}>구인게시판</MenuItem>
                <MenuItem value={"notice"}>공지사항</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          {SelectLanguage}
          <Grid item>
            <TextField
              className="board title"
              id="board_title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              maxRows={1}
              placeholder={"제목"}
              fullWidth
            ></TextField>
          </Grid>
          <Grid item>
            <EditorToolbar/>
            <ReactQuill value={content} modules={modules} formats={formats} onChange={content => setContent(content)} />
            {/* value: {content} */}
          </Grid>
          {SelectPoint}
          <Grid item>
            <Button
              className="board button"
              variant="outlined"
              disableElevation
              onClick={handleInputClick}
            >
              게시
            </Button>
          </Grid>
        </Grid>
      </Container>
    </>
  );
};

/*
 * 구인 게시판에서 구인 조건을 작성할 때 사용
 */
const Condition = () => {
  return (
    <Grid container spacing={4} direction="column" justifyContent="space-around">
      <Grid item>
        <TextField
          required
          label="필수"
          placeholder="필수 조건을 기입하세요."
        ></TextField>
        <TextField
          label="우대 사항"
          placeholder="우대 사항을 기입하세요."
        ></TextField>
      </Grid>
    </Grid>
  );
};

export default BoardWrite;
