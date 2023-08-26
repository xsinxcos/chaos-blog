package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.domain.ResponseResult;
import com.chaos.constants.SystemConstants;
import com.chaos.domain.dto.AddLinkDto;
import com.chaos.domain.dto.ChangeLinkStatusDto;
import com.chaos.domain.dto.ListLinkDto;
import com.chaos.domain.dto.UpdateLinkDto;
import com.chaos.domain.entity.Link;
import com.chaos.domain.vo.AdminLinkListVo;
import com.chaos.domain.vo.PageVo;
import com.chaos.utils.BeanCopyUtils;
import com.chaos.domain.vo.LinkVo;
import com.chaos.service.LinkService;
import org.springframework.stereotype.Service;
import com.chaos.mapper.LinkMapper;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.BrokenBarrierException;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-08-06 00:54:24
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        //查询出所有的审核通过的友链
        queryWrapper.eq(Link::getStatus , SystemConstants.LINK_STATUS_NORMAL);
        //转换成Vo
        List<Link> links = list(queryWrapper);
        return ResponseResult.okResult(BeanCopyUtils.copyBeanList(links , LinkVo.class));
    }

    @Override
    public ResponseResult listLink(ListLinkDto listLinkDto) {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        //根据友链名称进行模糊搜索
        wrapper.like(!Objects.isNull(listLinkDto.getName()) && StringUtils.hasText(listLinkDto.getName()),
                Link::getName ,listLinkDto.getName());
        //根据状态进行查询
        wrapper.eq(!Objects.isNull(listLinkDto.getStatus()) && StringUtils.hasText(listLinkDto.getStatus()),
                Link::getStatus ,listLinkDto.getStatus());
        //进行分页
        Page<Link> page = new Page<>(listLinkDto.getPageNum() ,listLinkDto.getPageSize());
        page(page ,wrapper);
        List<Link> records = page.getRecords();
        List<AdminLinkListVo> vos = BeanCopyUtils.copyBeanList(records, AdminLinkListVo.class);
        return ResponseResult.okResult(new PageVo(vos ,page.getTotal()));
    }

    @Override
    public ResponseResult addLink(AddLinkDto addLinkDto) {
        Link link = BeanCopyUtils.copyBean(addLinkDto, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLinkById(Long id) {
        Link link = getById(id);
        AdminLinkListVo vo = BeanCopyUtils.copyBean(link ,AdminLinkListVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateLink(UpdateLinkDto updateLinkDto) {
        LambdaUpdateWrapper<Link> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Link::getId,updateLinkDto.getId())
                .set(!Objects.isNull(updateLinkDto.getAddress()) && StringUtils.hasText(updateLinkDto.getAddress()) ,
                        Link::getAddress ,updateLinkDto.getAddress())
                .set(!Objects.isNull(updateLinkDto.getDescription()) && StringUtils.hasText(updateLinkDto.getDescription()) ,
                        Link::getDescription ,updateLinkDto.getDescription())
                .set(!Objects.isNull(updateLinkDto.getLogo()) && StringUtils.hasText(updateLinkDto.getLogo()) ,
                        Link::getLogo ,updateLinkDto.getLogo())
                .set(!Objects.isNull(updateLinkDto.getName()) && StringUtils.hasText(updateLinkDto.getName()) ,
                        Link::getName ,updateLinkDto.getName())
                .set(!Objects.isNull(updateLinkDto.getStatus()) && StringUtils.hasText(updateLinkDto.getStatus()) ,
                        Link::getStatus ,updateLinkDto.getStatus());
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLink(Long id) {
        LambdaUpdateWrapper<Link> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Link::getId ,id)
                .set(Link::getDelFlag ,SystemConstants.LINK_DELETE);
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeLinkStatus(ChangeLinkStatusDto dto) {
        LambdaUpdateWrapper<Link> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Link::getId ,dto.getId())
                .set(Link::getStatus ,dto.getStatus());
        update(updateWrapper);
        return ResponseResult.okResult();
    }
}

