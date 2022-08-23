package br.com.ecge.ecgefoods.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

public class DialogFragment extends android.support.v4.app.DialogFragment {

    private Map<String, Task> tasks = new HashMap<String, Task>();
    private ProgressDialog progress;

    public void startTask(String cod, TaskListener listener) {
        startTask(cod, listener, 0);
    }

    public void startTask(String cod, TaskListener listener, int progressId) {
        Log.d("ecgefoods", "startTask: " + cod);

        Task task = this.tasks.get(cod);
        if (task == null) {
            // Somente executa se já não está executando
            task = new Task(cod, listener, progressId);
            this.tasks.put(cod, task);
            task.execute();
        }
    }

    public interface TaskListener<T> {
        // Executa em background e retorna o objeto
        T execute() throws Exception;

        // Atualiza a view na UI Thread
        void updateView(T response);

        // Chamado caso o método execute() lance uma exception
        void onError(Exception exception);

        // Chamado caso a task tenha sido cancelada
        void onCancelled(String cod);
    }

    private class Task extends AsyncTask<Void, Void, TaskResult> {

        private String cod;
        private TaskListener listener;
        private int progressId;

        private Task(String cod, TaskListener listener, int progressId) {
            this.cod = cod;
            this.listener = listener;
            this.progressId = progressId;
        }

        @Override
        protected void onPreExecute() {
            Log.d("livroandroid", "task onPreExecute()");
            showProgress(this, progressId);
        }

        @Override
        protected TaskResult doInBackground(Void... params) {
            TaskResult r = new TaskResult();
            try {
                r.response = listener.execute();
            } catch (Exception e) {
                Log.e("livroandroid", e.getMessage(), e);
                r.exception = e;
            }
            return r;
        }

        protected void onPostExecute(TaskResult result) {
            try {
                if (result != null) {
                    if (result.exception != null) {
                        listener.onError(result.exception);
                    } else {
                        listener.updateView(result.response);
                    }
                }
            } finally {
                tasks.remove(cod);
                closeProgress(progressId);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            tasks.remove(cod);
            listener.onCancelled(cod);
        }
    }

    private class TaskResult<T> {
        private T response;
        private Exception exception;
    }

    private void closeProgress(int progressId) {
        if (progressId > 0 && getView() != null) {
            View view = getView().findViewById(progressId);
            if (view != null) {
                if (view instanceof SwipeRefreshLayout) {
                    SwipeRefreshLayout srl = (SwipeRefreshLayout) view;
                    srl.setRefreshing(false);
                } else {
                    view.setVisibility(View.GONE);
                }
                return;
            }
        }

        Log.d("livroandroid", "closeProgress()");
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
            progress = null;
        }
    }

    protected void showProgress(final Task task, int progressId) {
        if (progressId > 0 && getView() != null) {
            View view = getView().findViewById(progressId);
            if (view != null) {
                if (view instanceof SwipeRefreshLayout) {
                    SwipeRefreshLayout srl = (SwipeRefreshLayout) view;
                    if (!srl.isRefreshing()) {
                        srl.setRefreshing(true);
                    }
                } else {
                    view.setVisibility(View.VISIBLE);
                }
                return;
            }
        }

        // Mostra o dialog e permite cancelar
        if (progress == null) {
            progress = ProgressDialog.show(getActivity(), "Aguarde", "Por favor aguarde...");
            progress.setCancelable(true);
            progress.setOnCancelListener(dialog -> {
                // Cancela a AsyncTask
                task.cancel(true);
            });
        }
    }

    protected Button.OnClickListener cancelar() {
        return v -> this.dismiss();
    }
}
