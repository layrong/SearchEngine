package cn.ict.cluster;

import java.util.ArrayList;

import cn.ict.dataBean.TermBean;

public class Cluster {

		private ArrayList<TermBean> preCenterVector = null;/* 前一个类的中心 */
		private ArrayList<TermBean> centerVector = null;/* 当前类的中心 */
		private ArrayList<String> elements = null;/* 本类中包含的文档 */

		public void insertElement(String id) {
			this.elements.add(id);
		}

		public Cluster() {
			elements = new ArrayList<String>();
		}

		public ArrayList<TermBean> getPreCenterVector() {
			return preCenterVector;
		}

		public void setPreCenterVector(ArrayList<TermBean> preCenterVector) {
			this.preCenterVector = preCenterVector;
		}

		public ArrayList<TermBean> getCenterVector() {
			return centerVector;
		}

		public void setCenterVector(ArrayList<TermBean> centerVector) {
			this.centerVector = centerVector;
		}

		public void setElements(ArrayList<String> elements) {
			this.elements = elements;
		}

		public ArrayList<String> getElements() {
			return this.elements;
		}

	}