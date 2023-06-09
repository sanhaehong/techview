package com.sanhaehong.project.techview.controller;

import com.sanhaehong.project.techview.controller.argument.LogInUser;
import com.sanhaehong.project.techview.domain.answer.Answer;
import com.sanhaehong.project.techview.domain.question.Category;
import com.sanhaehong.project.techview.domain.question.Question;
import com.sanhaehong.project.techview.dto.AnswerDto;
import com.sanhaehong.project.techview.dto.AddQuestionDto;
import com.sanhaehong.project.techview.security.SessionUser;
import com.sanhaehong.project.techview.service.AnswerService;
import com.sanhaehong.project.techview.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @ModelAttribute("categories")
    public Category[] AddCategoryView() {
        return Category.values();
    }

    @GetMapping("/question/lists")
    public String findQuestion(@RequestParam(required = false) String content,
                               @RequestParam(required = false) Category category,
                               @PageableDefault Pageable pageable,
                               Model model) {
        Page<Question> questionPages;
        if(content == null && category == null) {
            questionPages = questionService.findPageAll(pageable);
        }
        else {
            questionPages = questionService.findPage(content, category, pageable);
        }
        model.addAttribute("questions", questionPages.stream().toList());
        model.addAttribute("totalQuestion", questionPages.getTotalElements());
        model.addAttribute("totalPage", questionPages.getTotalPages());
        return "question/question_list";
    }

    @GetMapping("/question/add")
    public String addQuestion(@ModelAttribute(name = "question") AddQuestionDto addQuestionDto) {
        return "question/question_add";
    }

    @PostMapping("/question/add")
    public String addQuestion(@Valid @ModelAttribute(name = "question") AddQuestionDto addQuestionDto,
                              BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "question/question_add";
        }
        questionService.save(addQuestionDto);
        return "redirect:/question/lists?page=0";
    }

    @GetMapping("/question/view/{id}")
    public String viewQuestion(@PathVariable Long id,
                               @ModelAttribute("answerDto") AnswerDto answerDto,
                               Model model) {
        Question question = questionService.findQuestion(id);
        List<Answer> answers = questionService.findAnswers(id);
        model.addAttribute("question", question);
        model.addAttribute("answers", answers);
        return "question/question_view";
    }

    @PostMapping("/question/{questionId}/answer/add")
    public String addAnswer(@PathVariable Long questionId,
                            @Valid @ModelAttribute("answerDto") AnswerDto answerDto,
                            BindingResult bindingResult,
                            @LogInUser SessionUser user,
                            RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "question/question_view";
        }
        answerService.addAnswer(user.getId(), questionId, answerDto.getContent());
        redirectAttributes.addAttribute("questionId", questionId);
        return "redirect:/question/view/{questionId}";
    }

    @PostMapping("/question/{questionId}/answer/update/{answerId}")
    public String updateAnswer(@PathVariable Long questionId,
                            @Valid @ModelAttribute("answerDto") AnswerDto answerDto,
                            @PathVariable Long answerId,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "question/question_view";
        }
        answerService.updateAnswer(answerId, answerDto.getContent());
        redirectAttributes.addAttribute("questionId", questionId);
        return "redirect:/question/view/{questionId}";
    }

    @PostMapping("/question/{questionId}/answer/delete/{answerId}")
    public String deleteAnswer(@PathVariable Long questionId,
                            @PathVariable Long answerId,
                            RedirectAttributes redirectAttributes) {
        answerService.deleteAnswer(answerId);
        redirectAttributes.addAttribute("questionId", questionId);
        return "redirect:/question/view/{questionId}";
    }
}
