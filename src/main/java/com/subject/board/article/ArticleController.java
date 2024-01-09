package com.subject.board.article;

import com.subject.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
  private final ArticleService articleService;
  private final BoardService boardService;

  // Read, 전체 보기
  @GetMapping
  public String readAll(Model model) {
    model.addAttribute("articles", articleService.readAll());
    return "article/home";
  }

  // Create
  @PostMapping("/create")
  public String create(
    @RequestParam("board-id") Long boardId,
    @RequestParam("title") String title,
    @RequestParam("content") String content,
    @RequestParam("password") String password
  ) {
    articleService.create(boardId, title, content, password);
    return "redirect:/article";
  }

  // Read, 상세 보기(완료)
  @GetMapping("/{articleId}")
  public String readOne(
    @PathVariable("articleId") Long articleId,
    Model model
  ) {
    model.addAttribute("article", articleService.readOne(articleId));
    return "article/read";
  }

  // Update
  // 비밀번호 화면
  @GetMapping("/{articleId}/password-view/update")
  public String passwordViewUpdate(
    @PathVariable("articleId") Long articleId,
    Model model
  ) {
    model.addAttribute("article", articleService.readOne(articleId));
    return "article/checkPasswordUpdate";
  }

  // 비밀번호 확인 -> update view(확인)
  @PostMapping("/{articleId}/passwordCheck/update")
  public String checkPasswordUpdate(
    @PathVariable("articleId") Long articleId,
    @RequestParam("input-password") String inputPassword,
    Model model
  ) {
    String password = articleService.readOne(articleId).getPassword();
    if (password.equals(inputPassword)) {
      model.addAttribute("article", articleService.readOne(articleId));
      model.addAttribute("boards", boardService.readAll());
      return "article/update";
    } else {
      model.addAttribute("article", articleService.readOne(articleId));
      model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
      return "article/checkPasswordUpdate";
    }
  }

  // update 실행(확인)
  @PostMapping("/{articleId}/update")
  public String update(
    @PathVariable("articleId") Long articleId,
    @RequestParam("board-id") Long boardId,
    @RequestParam("title") String title,
    @RequestParam("content") String content
  ) {
    articleService.update(articleId, boardId, title, content);
    return String.format("redirect:/article/%d", articleId);
  }

  // Delete
  @GetMapping("/{articleId}/password-view/delete")
  public String passwordViewDelete(
    @PathVariable("articleId") Long articleId,
    Model model
  ) {
    model.addAttribute("article", articleService.readOne(articleId));
    return "article/checkPasswordDelete";
  }

  @PostMapping("/{articleId}/passwordCheck/delete")
  public String checkPasswordDelete(
    @PathVariable("articleId") Long articleId,
    @RequestParam("input-password") String inputPassword,
    Model model
  ) {
    String password = articleService.readOne(articleId).getPassword();
    if (password.equals(inputPassword)) {
      articleService.delete(articleId);
      return "redirect:/article";
    } else {
      model.addAttribute("article", articleService.readOne(articleId));
      model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
      return "article/checkPasswordDelete";
    }
  }
}
