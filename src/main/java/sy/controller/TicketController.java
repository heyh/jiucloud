package sy.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sy.model.po.GCPo;
import sy.model.po.TTicket;
import sy.pageModel.*;
import sy.service.GCPoServiceI;
import sy.service.TicketServiceI;
import sy.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

/**
 * Created by heyh on 16/5/17.
 */

@Controller
@RequestMapping("/ticket")
public class TicketController extends BaseController {

    @Autowired
    private TicketServiceI ticketService;

    @Autowired
    private GCPoServiceI gcPoService;

    /**
     * 销项发票列表页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/outlist")
    public String outlist(HttpServletRequest request) {
        request.setAttribute("ticketType", "0"); // 销项发票
        return "/app/ticket/ticket_list";
    }

    /**
     * 进项发票列表页面
     * @param request
     * @return
     */
    @RequestMapping("/inlist")
    public String inlist(HttpServletRequest request) {
        request.setAttribute("ticketType", "1");
        return "/app/ticket/ticket_list";
    }

    /**
     * 列表
     *
     * @param ticket
     * @param ph
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("/dataGrid")
    @ResponseBody
    public DataGrid dataGrid(Ticket ticket, PageHelper ph,
                             HttpServletRequest request, HttpSession session) {
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        List<Integer> ugroup = sessionInfo.getUgroup();
        String keyword = request.getParameter("keyword") == null ? "" : request.getParameter("keyword");
        String ticketType = request.getParameter("ticketType") == null ? "" : request.getParameter("ticketType");
        if (null == ticket.getStartTime() && null == ticket.getEndTime()) {
            ticket.setStartTime(UtilDate.getshortFirst() + " 00:00:00");
            ticket.setEndTime(UtilDate.getshortLast() + " 23:59:59");
        }
        DataGrid dataGrid = ticketService.dataGrid(ticket, ph, ugroup, ticketType, keyword);
        return dataGrid;
    }

    /**
     * 跳转新增页面
     *
     * @return
     */
    @RequestMapping("/toAddPage")
    public String toAddPage(HttpServletRequest request) {
        request.setAttribute("ticketType", request.getParameter("ticketType"));
        return "/app/ticket/ticket_add";
    }

    /**
     * 新增
     *
     * @param tTicket
     * @param request
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Json add(TTicket tTicket, HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        Json j = new Json();
        tTicket.setUid(sessionInfo.getId());
        tTicket.setCid(sessionInfo.getCompid());
        tTicket.setUname((sessionInfo.getName() != null && !sessionInfo.getName().equals("")) ? sessionInfo.getName() : sessionInfo.getUsername());
        tTicket.setCompany(sessionInfo.getCompName());
        tTicket.setId(UuidUtil.get32UUID());
        try {
            this.ticketService.add(tTicket);
            j.setObj(tTicket.getId());
            j.setMsg("新增成功！");
            j.setSuccess(true);
        } catch (Exception ex) {
            j.setMsg("新增失败");
            j.setSuccess(false);
        }
        return j;
    }

    /**
     * 文件上传
     * @param req
     * @param rt
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public Json uploadFile(HttpServletRequest req, MultipartHttpServletRequest rt) {
        Json j = new Json();
        SessionInfo sessionInfo = (SessionInfo) req.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        String userPath = sessionInfo.getId() + "/";
        String mid = rt.getParameter("mid");
        if (mid == null || mid.length() == 0 || mid.equals("undefined")) {
            j.setCode(000);
            j.setSuccess(false);
            j.setMsg("mid is need not null");
            return j;
        }
        try {
            String file_path = PropertyUtil.getFileRealPath() + "/upload/" + Constant.SOURCE + userPath;
            MultipartFile patch =  rt.getFile("file");// 获取文件
            String fileName = patch.getOriginalFilename();// 得到文件名
            if (!patch.isEmpty()) {
                File saveDir = new File(file_path);
                if (!saveDir.exists())
                    saveDir.mkdirs();
                String reg = fileName.substring(
                        patch.getOriginalFilename().lastIndexOf(".") + 1)
                        .toLowerCase();
                int status = Constant.fileStatus(reg);
                if (status == -1) {
                    j.setSuccess(false);
                    j.setMsg("上传的文件格式不支持");
                    return j;
                }
               String finalname = UUID.randomUUID().toString();
                // end
                File f = new File(file_path + finalname + "." + reg);
                patch.transferTo(f);

                GCPo gcpo = new GCPo();
                gcpo.setMpid(mid);
                gcpo.setFileName(patch.getOriginalFilename());
                gcpo.setPdfFilePath("");
                gcpo.setSwfFilePath("");
                gcpo.setSourceFilePath(userPath + finalname + "." + reg);
                gcpo.setExt(reg);
                gcpo.setStatus(status);

                // 如果已经是pdf直接设置从pdf > swf状态开始
                if (reg.equals("pdf")) {
                    String pdfFilePath = PropertyUtil.getFileRealPath() + "/upload/" + Constant.PDFSOURCE + userPath;
                    FileUtils.copyFileToDirectory(f, new File(pdfFilePath));
                    gcpo.setStatus(Constant.PDF2SWF_STATUS);
                    gcpo.setPdfFilePath(userPath + finalname + "." + reg);
                }
                gcPoService.add(gcpo);
                j.setMsg(fileName + "上传成功");
                j.setObj(gcpo.getId());
                j.setCode(2000);
                j.setSuccess(true);
                return j;
            } else {
                j.setCode(1004);
                j.setObj(null);
                j.setSuccess(false);
                j.setMsg("上传的文件不存在");
                return j;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            j.setCode(1005);
            j.setObj(null);
            j.setSuccess(false);
            j.setMsg("上传异常:" + ex.getMessage());
            return j;
        }
    }

    /**
     * 跳转修改页面
     *
     * @param request
     * @param response
     * @return
     * @throws java.io.IOException
     */
    @RequestMapping("/toEditPage")
    public String toEditPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        TTicket tTicket = ticketService.detail(id);
        if (tTicket == null) {
            response.getWriter().write("1");
            return null;
        }

        request.setAttribute("tTicket", tTicket);
        return "/app/ticket/ticket_edit";
    }

    @RequestMapping("/edit")
    @ResponseBody
    public Json edit(TTicket tTicket, HttpServletRequest request) {
        Json j = new Json();
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        tTicket.setUid(sessionInfo.getId());
        tTicket.setUname((sessionInfo.getName() != null && !sessionInfo.getName().equals("")) ? sessionInfo.getName() : sessionInfo.getUsername());
        tTicket.setUpdateTime(new Date());
        try {
            ticketService.update(tTicket);
            j.setSuccess(true);
            j.setMsg("修改成功！");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Json delete(String id) {
        Json j = new Json();
        try {
            ticketService.delete(id);
            j.setSuccess(true);
            j.setMsg("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /* 后台导出到excel */
    @RequestMapping("/securi_execl")
    public void OutputToExcel(Ticket ticket, PageHelper ph,
                              HttpServletResponse response, HttpServletRequest request) {
        response.setContentType("text/html;charset=utf8");
        String keyword = request.getParameter("keyword") == null ? "" : request.getParameter("keyword");
        String ticketType = request.getParameter("ticketType") == null ? "" : request.getParameter("ticketType");
        HttpSession session = request.getSession();

        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        List<Integer> ugroup = sessionInfo.getUgroup();
        ph.setRows(999999999);

        List<Ticket> datas = ticketService.dataGrid(ticket, ph, ugroup, ticketType, keyword).getRows();

        List<Map<String, Object>> map = createExcelRecord(datas);

        // 填充数据
        String[] columnNames = {"发票名称", "开票日期", "合同", "供应商名称", "纳税识别号", "地址", "纳税账户开户银行", "纳税账户开户账号", "纳税资格状况",
                "单位", "数量", "单价", "规格型号", "金额", "联系电话", "入库时间", "操作人"}; // 列名
        String[] keys = {"ticketName", "ticketDate", "contract", "supplier", "taxNo", "address", "taxBank", "taxAccount", "taxStatus",
                "unit", "count", "price", "specifications", "money", "linkPhone", "createTime", "uname"};// map中的key

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            ExcelExportUtil.createWorkBook(map, keys, columnNames).write(os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=data.xls");
            ServletOutputStream out;
            out = response.getOutputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    // 格式化execl表格数据
    private List<Map<String, Object>> createExcelRecord(List<Ticket> tickets) {
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "data.xls");
        items.add(map);

        for (Ticket ticket : tickets) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("ticketName", ticket.getTicketName());
            item.put("ticketDate", ticket.getTicketDate());
            item.put("contract", ticket.getContract());
            item.put("supplier", ticket.getSupplier());
            item.put("taxNo", ticket.getTaxNo());
            item.put("address", ticket.getAddress());
            item.put("taxBank", ticket.getTaxBank());
            item.put("taxAccount", ticket.getTaxAccount());
            item.put("taxStatus", ticket.getTaxStatus());
            item.put("unit", ticket.getUnit());
            item.put("count", ticket.getCount());
            item.put("price", ticket.getPrice());
            item.put("specifications", ticket.getSpecifications());
            item.put("money", ticket.getMoney());
            item.put("linkPhone", ticket.getLinkPhone());
            item.put("createTime", ticket.getCreateTime());
            item.put("uname", ticket.getUname());
            items.add(item);

        }
        return items;
    }
}
