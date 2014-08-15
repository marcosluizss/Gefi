package br.com.ml2s.gefi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

    private Button btEntrar;
    private EditText etSenhaTela;
    private String txtSenhaBD = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btEntrar = (Button)findViewById(R.id.bt_entrar);
        etSenhaTela = (EditText)findViewById(R.id.et_login_senha);

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtSenhaTela = etSenhaTela.getText().toString();
                // buscar a senha de banco
                if(txtSenhaBD.equals(txtSenhaTela)){
                    //abre aplicação
                    Intent intent = new Intent(Login.this,Home.class);
                    startActivity(intent);
                }else{
                    //exibe mensagem de erro
                    String txtMsgErro = getString(R.string.login_msg_erro);
                    Toast alerta = Toast.makeText(Login.this, txtMsgErro,Toast.LENGTH_SHORT);
                    alerta.show();
                }
            }
        });

    }

}
