package dambi.probak;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.Statement;

import dambi.atzipenekoak.Xmla;
import dambi.pojoak.Bezeroak;


public class BezeroakXmlInportatu {
    public static void main(String[] args) {
        
        Xmla xmla = new Xmla("data/Bezeroak.xml");
        Bezeroak bezeroak = new Bezeroak();

        Connection c  = null;
        Statement stmt = null;

        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/1Ariketa","aitzol", "aitzol");

            System.out.println("Successfully Connected.");

            bezeroak = xmla.irakurriBezeroak();

            PreparedStatement st = null;
           for(int i = 0;i < bezeroak.getBezeroak().size();i++){
                st = c.prepareStatement("INSERT INTO public.\"res_partner\"(id,name,street,zip,city,email,phone,mobile) values (?,?,?,?,?,?,?,?)");
                st.setInt(1, bezeroak.getBezeroak().get(i).getId());
                st.setString(2, bezeroak.getBezeroak().get(i).getName());
                st.setString(3, bezeroak.getBezeroak().get(i).getStreet());
                st.setInt(4, bezeroak.getBezeroak().get(i).getZip());
                st.setString(5, bezeroak.getBezeroak().get(i).getCity());
                st.setString(6, bezeroak.getBezeroak().get(i).getEmail());
                st.setInt(7, bezeroak.getBezeroak().get(i).getPhone());
                st.setInt(8, bezeroak.getBezeroak().get(i).getMobile());
                st.executeUpdate();
            }

            st.close();

            
            //System.out.println(bezeroak);

            c.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
