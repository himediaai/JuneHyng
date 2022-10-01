package com.himedia.jbshop.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.himedia.jbshop.goods.ImageFileVO;

import net.coobird.thumbnailator.Thumbnails;

@Controller
public class FileController {

	//이미지 파일 다운로드 및 업로드 경로
	private static final String CURR_IMAGE_REPO_PATH = "C:/shopping/file_repo";

	//경로상 저장된 이미지 파일 내보내기(다운로드)
	@RequestMapping("/download")
	protected void download(@RequestParam("fileName") String fileName, @RequestParam("goods_id") String goods_id,
			HttpServletResponse response) throws Exception {
		OutputStream out = response.getOutputStream();
		String filePath = CURR_IMAGE_REPO_PATH + "/" + goods_id + "/" + fileName;
		File image = new File(filePath);

		response.setHeader("Cache-Control", "no-cache");
		response.addHeader("Content-disposition", "attachment; fileName=" + fileName);
		FileInputStream in = new FileInputStream(image);
		byte[] buffer = new byte[1024 * 8];
		while (true) {
			int count = in.read(buffer);
			if (count == -1)
				break;
			out.write(buffer, 0, count);
		}
		in.close();
		out.close();
	}

	//파일 경로에 저장(업로드)하기 
	protected List<ImageFileVO> upload(MultipartHttpServletRequest multipartRequest) throws Exception {
		List<ImageFileVO> fileList = new ArrayList<ImageFileVO>();
		Iterator<String> fileNames = multipartRequest.getFileNames();	//다중 파일이름 가져오기
		while (fileNames.hasNext()) {
			ImageFileVO imageFileVO = new ImageFileVO();
			String fileName = fileNames.next();
			imageFileVO.setFileType(fileName);							//파일 하나씩 타입지정하기(fileName)
			MultipartFile mFile = multipartRequest.getFile(fileName);	//파일 하나씩 가져오기(mfile)
			String originalFileName = mFile.getOriginalFilename();		//파일 하나씩 원본이름 가져오기(originalFileName)
			imageFileVO.setFileName(originalFileName);
			fileList.add(imageFileVO);

			File file = new File(CURR_IMAGE_REPO_PATH + "/" + fileName);//묶음파일별 폴더생성하기
			if (mFile.getSize() != 0) {
				if (!file.exists()) {									//폴더가 없으면 폴더와 파일 생성하기
					if (file.getParentFile().mkdirs()) {
						file.createNewFile();
					}
				}
				mFile.transferTo(new File(CURR_IMAGE_REPO_PATH + "/" + "temp" + "/" + originalFileName));//폴더 있으면 Temp에서 가져오기
			}
		}
		return fileList;//묶음파일정보(imageFileVO) 리턴
	}

	//파일 및 폴더 삭제하기
	private void deleteFile(String fileName) {
		File file = new File(CURR_IMAGE_REPO_PATH + "/" + fileName);
		try {
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//이미지 파일 사이즈(121,154) 및 포맷(.png) 변경해서 내보내기
	@RequestMapping("/thumbnails.do")
	protected void thumbnails(@RequestParam("fileName") String fileName, @RequestParam("goods_id") String goods_id,
			HttpServletResponse response) throws Exception {
		OutputStream out = response.getOutputStream();
		String filePath = CURR_IMAGE_REPO_PATH + "/" + goods_id + "/" + fileName;
		File image = new File(filePath);

		if (image.exists()) {
			Thumbnails.of(image).size(121, 154).outputFormat("png").toOutputStream(out);
		}
		byte[] buffer = new byte[1024 * 8];
		out.write(buffer);
		out.close();
	}
}
