package com.yjq.programmer.service.impl;

import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.ArticleMapper;
import com.yjq.programmer.dao.LikeMapper;
import com.yjq.programmer.domain.Article;
import com.yjq.programmer.domain.Like;
import com.yjq.programmer.domain.LikeExample;
import com.yjq.programmer.dto.LikeDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.ILikeService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.CopyUtil;
import com.yjq.programmer.utils.UuidUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-02-18 15:15
 */
@Service
@Transactional
public class LikeServiceImpl implements ILikeService {

    @Resource
    private LikeMapper likeMapper;

    @Resource
    private ArticleMapper articleMapper;

    /**
     * 点赞文章
     * @param likeDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> likeArticle(LikeDTO likeDTO) {
        if(CommonUtil.isEmpty(likeDTO.getArticleId()) || CommonUtil.isEmpty(likeDTO.getUserId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Article article = articleMapper.selectByPrimaryKey(likeDTO.getArticleId());
        if(article == null) {
            return ResponseDTO.errorByMsg(CodeMsg.ARTICLE_NOT_EXIST);
        }
        Like like = CopyUtil.copy(likeDTO, Like.class);
        like.setId(UuidUtil.getShortUuid());
        like.setCreateTime(new Date());
        if(likeMapper.insertSelective(like) == 0 ) {
            return ResponseDTO.errorByMsg(CodeMsg.LIKE_ERROR);
        }
        // 更新文章的点赞数
        article.setLikeNum(article.getLikeNum() + 1);
        articleMapper.updateByPrimaryKeySelective(article);

        return ResponseDTO.successByMsg(true, "点赞成功");
    }

    /**
     * 取消点赞文章
     * @param likeDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> unlikeArticle(LikeDTO likeDTO) {
        if(CommonUtil.isEmpty(likeDTO.getArticleId()) || CommonUtil.isEmpty(likeDTO.getUserId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Article article = articleMapper.selectByPrimaryKey(likeDTO.getArticleId());
        if(article == null) {
            return ResponseDTO.errorByMsg(CodeMsg.ARTICLE_NOT_EXIST);
        }
        LikeExample likeExample = new LikeExample();
        likeExample.createCriteria().andArticleIdEqualTo(likeDTO.getArticleId()).andUserIdEqualTo(likeDTO.getUserId());
        if(likeMapper.deleteByExample(likeExample) == 0 ) {
            return ResponseDTO.errorByMsg(CodeMsg.UNLIKE_ERROR);
        }
        // 更新文章的点赞数
        article.setLikeNum(article.getLikeNum() - 1);
        articleMapper.updateByPrimaryKeySelective(article);

        return ResponseDTO.successByMsg(true, "取消点赞成功");
    }

    /**
     * 判断是否喜欢文章
     * @param likeDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> judgeLike(LikeDTO likeDTO) {
        if(CommonUtil.isEmpty(likeDTO.getArticleId()) || CommonUtil.isEmpty(likeDTO.getUserId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Article article = articleMapper.selectByPrimaryKey(likeDTO.getArticleId());
        if(article == null) {
            return ResponseDTO.errorByMsg(CodeMsg.ARTICLE_NOT_EXIST);
        }
        LikeExample likeExample = new LikeExample();
        likeExample.createCriteria().andArticleIdEqualTo(likeDTO.getArticleId()).andUserIdEqualTo(likeDTO.getUserId());
        List<Like> likeList = likeMapper.selectByExample(likeExample);
        if(likeList.size() > 0) {
            return ResponseDTO.success(true);
        } else {
            return ResponseDTO.success(false);
        }
    }

}
