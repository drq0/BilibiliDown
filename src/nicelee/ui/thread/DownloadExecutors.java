package nicelee.ui.thread;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DownloadExecutors {

	final static Comparator<DownloadRunnableInternal> comp;
	static {
		comp = new Comparator<DownloadRunnableInternal>() {
			@Override
			public int compare(DownloadRunnableInternal o1, DownloadRunnableInternal o2) {
				// o1.invokeByContinueTask 为true时优先级更高
				// o1.urlTimestamp 越小优先级更高
				if (o1.invokeByContinueTask == o2.invokeByContinueTask) {
					return (int) (o1.urlTimestamp - o2.urlTimestamp);
				} else {
					return o1.invokeByContinueTask ? -1 : 1;
				}
			}
		};
	}

	/**
	 * <p>同Executors.newFixedThreadPool(int nThreads)</p>
	 * <p>将队列由 LinkedBlockingQueue<Runnable> 改为 PriorityBlockingQueue<DownloadRunnableInternal></p>
	 * @param nThreads
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ExecutorService newPriorityFixedThreadPool(int nThreads) {
		@SuppressWarnings("rawtypes")
		PriorityBlockingQueue queue = new PriorityBlockingQueue<DownloadRunnableInternal>(11, comp);
		return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, queue);
	}
}
